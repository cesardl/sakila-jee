# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4 / Java 21 REST backend over the MySQL "Sakila" sample database (films, actors, customers, rentals, etc.). It's a demo/portfolio app (`org.sanmarcux.samples.sakila`) exposing HATEOAS-style JSON APIs for a subset of the Sakila schema.

## Database setup

**The one hard requirement is MySQL 8.** Everything else below — port, database user, password,
container name — is configuration: change it freely, point `spring.datasource.*` at whatever you
used, and the app works. Change the *server version* and something will break, quietly.

Why 8 specifically: `explicit_defaults_for_timestamp` flipped from `0` to `1` in MySQL 8.0.2. On
5.7 an explicit `NULL` into a `TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP` column was silently
rewritten to the current time; on 8 it is `ERROR 1048: Column ... cannot be null`. That difference
already cost one live bug (`POST /actors`) that the 5.7 test container could not see. The
Testcontainer in `AbstractIntegrationTest` is pinned to 8 for exactly this reason — keep it and the
dev server on the same major version.

Local dev via Docker (port and credentials here are examples, not requirements):

```
docker run --name sakila-db -p 3312:3306 --restart on-failure -e MYSQL_DATABASE=sakila -e MYSQL_ROOT_PASSWORD=rootroot -e TZ='America/Lima' -d mysql:8.0 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --log_bin_trust_function_creators=1
```

Then load the scripts (below) and create the application account from the snippet at the top of
`database-model/queries.sql`. The account is made there rather than through `MYSQL_USER`/
`MYSQL_PASSWORD` because those env vars always attach a password, while the committed
`application.properties` ships with an empty one — so the two would contradict each other.
The snippet explains how to switch to a real password if you want one, including the
`caching_sha2_password` / `allowPublicKeyRetrieval` wrinkle that comes with it.

`TZ` is worth keeping in step with `serverTimezone` in the JDBC URL (`America/Lima` in both here).
Connector/J converts `TIMESTAMP` columns through the connection zone, so a mismatch between the dev
server and the test container makes timestamps differ by the offset in one environment only.

Load `database-model/` scripts **in this order** — `sakila-schema.sql`, then `sakila-data.sql`, then `auth-fixture.sql`. The first two are vendored from Oracle (own copyright header) — don't edit them; put schema changes in `auth-fixture.sql` or a new script. `sakila.mwb` is the MySQL Workbench model, kept in sync manually.

`auth-fixture.sql` replaced the old `data.sql`, which was a Travis-era minimal extraction whose rows now collide with the full dump.

The committed config points at `jdbc:mysql://localhost:3312/sakila`; app on `8181`, actuator on `127.0.0.1:8182`. Ports again: config, not contract.

## Common commands

```
mvn clean verify              # build + run tests + jacoco coverage report
mvn test                      # run tests only
mvn test -Dtest=FilmRestControllerTest                 # single test class
mvn test -Dtest=FilmRestControllerTest#readFilms       # single test method
mvn spring-boot:run           # run the app locally (needs the DB above)
```

There is no `mvnw` wrapper script at the repo root (only `.mvn/wrapper/`) — use the system `mvn` at `$M2_HOME` (`D:\developer-resources\apache-maven-3.6.3`, local repo `E:\M2_REPO`).

**The shell `JAVA_HOME` points at JDK 11, but this project needs Java 21.** IntelliJ is configured with JDK 21 so it builds there; from a terminal you must set it first, or every build fails on `release 21`:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
```

Maven 3.6.3 handles Boot 4 fine — no upgrade needed.

Tests are `@SpringBootTest` + `MockMvc` integration tests against a real MySQL (no mocking layer), asserting known fixture rows (e.g. film id 1 = "ACADEMY DINOSAUR", actor 1 = PENELOPE GUINESS — both require the *full* `sakila-data.sql`). The database is supplied by Testcontainers; see below.

## Architecture

Classic layered structure per resource (Film, Customer, Actor, ...), all under `org.sanmarcux.samples.sakila`:

```
controller/  → @RestController, thin — delegates to business layer, does HTTP concerns (status codes, Location header)
business/    → interface + impl/ (e.g. FilmBusiness / impl/FilmBusinessImpl) — orchestration, DTO↔entity mapping, HATEOAS assembly
dao/         → Spring Data JPA repositories, over dao/model/ JPA entities (generated from the Sakila schema)
dto/         → API-facing DTOs, separate from JPA entities
exceptions/  → domain exceptions + @RestControllerAdvice handlers (one advice class per exception, e.g. CustomerNotFoundAdvice)
config/      → security / OAuth2 config
```

Key conventions to preserve when touching this code:

- **Business layer always returns HATEOAS types** (`EntityModel<T>` / `CollectionModel<T>`), not raw DTOs — see `CustomerBusiness`/`FilmBusiness` and their `*ModelAssembler` (e.g. `CustomerModelAssembler`) which builds `self`/collection links via `WebMvcLinkBuilder.linkTo(methodOn(...))`. Follow this pattern for any new resource.
- **Entity ↔ DTO mapping goes through the shared `ModelMapper` bean** (configured in `SakilaApplication` with `STRICT` matching + skip-null), injected into business impls — don't hand-roll mapping code.
- **"Not found" is a per-entity exception + per-entity `@RestControllerAdvice`** (`XNotFoundException` / `XNotFoundAdvice`), not a single generic handler. `ResourceNotFoundException`/`OperationNotAllowedException` are the two generic exceptions used for cross-cutting cases (e.g. rejecting a payload that sets an ID on create).
- **JPA enum columns use an `AttributeConverter`**, not `@Enumerated` — see `Rating` + `RatingConverter` (maps enum ↔ the short DB code like `"PG-13"`) and `YearConverter`. Mirror this for new enum-backed columns.
- Repositories are plain `JpaRepository` interfaces in `dao/`; custom queries (e.g. `FilmRepository.findAllByActor`) live there, not in the business layer.
- **`last_update` is owned by the database, never by Java.** Every such column in Sakila is
  `TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP`, so all of them map as
  `@Column(..., insertable = false, updatable = false)` and no business code sets them. Leaving one
  insertable makes an omitted value an explicit `NULL`, which MySQL 8 rejects; leaving one updatable
  makes Hibernate write back the stale loaded value and suppress `ON UPDATE`. `customer.create_date`
  is the sole exception — it has no default, so `CustomerBusinessImpl.save()` sets it.
- **Don't substitute static values for payload data.** Resolve what the client sent (see
  `CustomerBusinessImpl.resolveStore`/`resolveAddress`, which 404 on an unknown id) and let
  `@Valid` + `@NotNull` reject what is genuinely required. Boxed types on optional flags:
  `CustomerDTO.active` is a `Boolean` so that "absent" means the column default rather than `false`.

## Security

JWT bearer tokens, all wired in `config/WebSecurityConfiguration.java`:

- `POST /auth/token` (`controller/AuthRestController`) authenticates against the `staff` table and returns an HS256 token; every other endpoint requires `Authorization: Bearer <token>`.
- Signing uses Spring Security's own Nimbus support (`spring-boot-starter-security-oauth2-resource-server`) — **no third-party JWT library**. Note the Boot 4 starter rename: `spring-boot-starter-oauth2-resource-server` is deprecated.
- The signing key comes from the `JWT_SECRET` env var via `${jwt.secret}` with **no fallback** — `application.properties` is committed, so a literal default would be a published key. The app won't start without it.
- Passwords use `DelegatingPasswordEncoder` (`{bcrypt}` prefix), so migrating to argon2 later is a rehash-on-login, not a schema migration. `staff.password` is widened to `VARCHAR(255)` by `database-model/auth-fixture.sql`.

Boot 4 / Security 7 gotchas already hit and solved here — don't reintroduce them:

- `DaoAuthenticationProvider` is **constructor-only**; `setUserDetailsService()` no longer exists.
- `NimbusJwtEncoder.withSecretKey(k).algorithm(...)` vs `NimbusJwtDecoder.withSecretKey(k).macAlgorithm(...)` — the builders name it differently.
- **The encoder omits the `typ` header unless you pass an explicit `JwsHeader`**, and the resource-server decoder then rejects the token with *"the given typ value needs to be one of [JWT]"*. `AuthRestController` sets `JwsHeader.with(...).type("JWT")` for this reason.
- CORS lives in the security chain (`http.cors(...)` + `CorsConfigurationSource`). Do **not** re-add a `FilterRegistrationBean` CORS filter or an `OPTIONS permitAll` rule — Spring's `CorsFilter` already short-circuits preflight before authorization.

## Tests

`mvn clean verify` — tests start a throwaway `mysql:8.0.46` Testcontainer seeded from `database-model/` (`AbstractIntegrationTest`), so they never touch the local dev DB. Docker must be running.

- The container is a **static field**, not a `@Bean`: Spring caches one context per distinct test configuration, and a bean would reload the 3.4MB dump per context.
- Init scripts run in **alphabetical** order inside the container, hence the `01-`/`02-`/`03-` prefixes.
- Resource tests use `@WithMockUser`; `AuthRestControllerTest` is the one test exercising real credentials and a real signed token.

No CI is configured — `.travis.yml` was deleted (Travis's OSS tier is gone, and it had silently been loading a fixture that could not satisfy the tests).
