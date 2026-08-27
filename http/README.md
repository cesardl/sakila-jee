# Test plan — Sakila JEE API

Two layers, deliberately different in what they touch:

| Layer | Runs on | Database | Safe to re-run |
|---|---|---|---|
| `mvn clean verify` (JUnit + MockMvc) | CI / any machine with Docker | throwaway Testcontainer | yes, always |
| `http/*.http` (IntelliJ HTTP Client) | a running app you point at | your **real dev DB** | mostly — see [Destructive files](#destructive-files) |

The JUnit layer is the gate. The `.http` layer is for exploring the live API and for
checking things MockMvc cannot see: real network status codes, CORS headers, the
`WWW-Authenticate` challenge, and **response latency**.

---

## 0. Prerequisites

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"   # shell JAVA_HOME points at 11
$env:JWT_SECRET = "<at least 32 bytes>"           # no default; app will not start without it
```

Dev DB (MySQL 5.7 on `3310`) must be up and seeded, in this order:
`sakila-schema.sql` → `sakila-data.sql` → `auth-fixture.sql`.

> **Re-run `auth-fixture.sql` before using this suite.** It now seeds a `disabled`
> staff row (`active = 0`) that test 8b in `auth.http` depends on. The file is
> idempotent (`INSERT IGNORE`); the two `ALTER` statements will report
> *Duplicate key name* on an already-migrated DB, which is expected and harmless.

Credentials live in `http-client.private.env.json`, which is gitignored. If it is
missing, copy the shape from `http-client.env.json` and add `username`/`password`.

---

## 1. Run the JUnit layer first

```powershell
mvn clean verify
```

28 tests, Testcontainers pulls `mysql:5.7.44` and seeds it from `database-model/`.
Docker must be running. If this is red, do not bother with the `.http` files.

## 2. Start the app

```powershell
mvn spring-boot:run     # API on 8181, actuator on 127.0.0.1:8182
```

## 3. Run the `.http` files

Order matters only in that each file logs in first. Any file can be run standalone —
`save-token.js` stores the token in a **global**, so it survives across files.

| File | Requests | Writes to DB | What a green run proves |
|---|---|---|---|
| `auth.http` | 11 | no | the API is closed by default and signatures are verified |
| `actors.http` | 15 | yes, self-cleaning | pagination, relationships, full CRUD lifecycle |
| `films.http` | 9 | no | read paths, sorting, rejection cases |
| `customers.http` | 10 | yes, self-cleaning | HATEOAS links, CRUD lifecycle |
| `films-write.http` | 3 | **yes, permanent** | film creation — see below |

### Destructive files

`films-write.http` is separated for a reason: `FilmRestController` exposes **no DELETE**,
so nothing it creates can be undone through the API. Clean up in SQL afterwards:

```sql
DELETE FROM film_text WHERE title LIKE 'HTTPCLIENT PROBE%';
DELETE FROM film      WHERE title LIKE 'HTTPCLIENT PROBE%';
```

`actors.http` and `customers.http` each end with a delete + a 404 check, so they clean
up after themselves. If the final 404 fails, a probe row survived — remove it before it
becomes permanent drift.

---

## 4. What the security tests actually assert

`auth.http` is the file worth reading. Each numbered request is a claim:

| # | Claim | Failure means |
|---|---|---|
| 2 | a valid token opens a protected endpoint | auth is broken |
| 3 | no token → 401 + `Bearer` challenge | the API is open |
| 4 | malformed token → 401 | — |
| 5 | **valid token + 1 char on the signature → 401** | signatures are parsed but not *verified* |
| 6/7 | wrong password and unknown user return byte-identical bodies | username enumeration |
| 8 | NULL-password staff row (`Jon`) → 401, not 500 | a 500 leaks that the row exists |
| 8b | **disabled account + correct password → 401** | disabled accounts can still log in |
| 9 | blank username → 400 | validation runs after auth |
| 10 | CORS preflight succeeds unauthenticated | the browser client breaks |

#5 is the one that separates authentication from decoration. #8b is new — see below.

### The timing check (#8b)

`staff.active` is mapped to `UserDetails.disabled()`, and `DaoAuthenticationProvider`
runs its pre-authentication checks *before* the bcrypt comparison. That made a disabled
account reject far faster than a wrong password, so latency alone identified which
accounts exist but are switched off. Measured on this machine, 12 runs each:

| Login attempt | Before fix | After fix |
|---|---|---|
| wrong password (pays bcrypt) | 171.2 ms | 107.6 ms |
| unknown user (dummy hash) | — | 104.3 ms |
| **disabled account, correct password** | **10.8 ms** | **102.3 ms** |
| valid login (also mints a JWT) | — | 134.3 ms |

A ~16× gap is trivially measurable over a network. The fix moves the status check to
`postAuthenticationChecks`, so every failure pays the same hash.

**How to regression-test it:** #8b asserts the 401 and the generic body, but the HTTP
Client cannot assert latency. To check timing by hand, run #6 and #8b back to back and
compare the times in the response pane. If #8b comes back an order of magnitude faster
than #6, the fix has been reverted.

---

## 5. Known bugs, pinned on purpose

Four assertions are **characterization tests**: they assert current, wrong behaviour so
that fixing it fails the suite loudly instead of silently changing the contract. Each is
commented in place. If one of these goes red, read the message — it is telling you
something got fixed, and the assertion should then be deleted.

| Where | Pinned behaviour | Correct behaviour |
|---|---|---|
| `actors.http` | PATCH maps to a fresh entity, nulling omitted fields (500 on partial payloads) | true partial update |
| `customers.http` | `save()` ignores payload `store`/`address`, hardcodes 2 / 591 | honour the payload |
| `customers.http` | PUT returns 201 | 200 |
| `films.http` | `film_id > 32767` → 500 (`Film.filmId` is `Short`, repo is typed `Integer`) | 404 |

The `Film.filmId` one is the same class of bug as `Staff.staffId`, which was `Byte`
against a `TINYINT UNSIGNED` column and is now `Integer`.

---

## 6. Database drift

`films.http` and `customers.http` log a WARNING (not a failure) when row counts drift
from the stock dump. Current state of the dev DB on 3310:

| Table | Expected | Actual | Drift |
|---|---|---|---|
| `film` | 1000 | 1009 | 1 `HTTPCLIENT`, 8 older probes |
| `customer` | 599 | 609 | 6 `HTTPCLIENT`, 4 older probes |

```sql
-- probes from this suite
DELETE FROM film_text WHERE title LIKE 'HTTPCLIENT%';
DELETE FROM film      WHERE title LIKE 'HTTPCLIENT%';
DELETE FROM customer  WHERE first_name LIKE 'HTTPCLIENT%';

-- older leftovers, inspect before deleting
SELECT film_id, title FROM film WHERE film_id > 1000;
SELECT customer_id, first_name, last_name FROM customer WHERE customer_id > 599;
```

Drift is cosmetic for the `.http` suite but **not** for `mvn verify` — those tests run
against a fresh container every time, which is exactly why they are the gate.

---

## 7. IntelliJ version note

IntelliJ returns `response.body` already parsed on some versions and as a raw JSON
string on others. Every handler that reads the body starts with:

```js
var body = typeof response.body === "string" ? JSON.parse(response.body) : response.body;
```

so the files work on 2020.1 and on current builds without edits. If you add a request,
copy that line rather than using `response.body` directly.
