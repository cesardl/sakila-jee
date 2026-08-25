# Sakila Sample Application [![Maintainability](https://api.codeclimate.com/v1/badges/77118783b8939faf1de2/maintainability)](https://codeclimate.com/github/cesardl/sakila-jee/maintainability) <a href="https://codeclimate.com/github/cesardl/sakila-jee/test_coverage"><img src="https://api.codeclimate.com/v1/badges/77118783b8939faf1de2/test_coverage" /></a>

Demo with Spring Boot and Sakila sample database from MySQL.

REST API secured with JWT bearer tokens, authenticating against the Sakila `staff` table.

## Requirements

- JDK 21
- Maven
- Docker (for the database, and for the tests)

## Database

```
docker run --name sakila-db -p 3310:3306 --restart on-failure -e MYSQL_DATABASE=sakila -e MYSQL_ROOT_PASSWORD=rootroot -e MYSQL_USER=travis -e MYSQL_PASSWORD=my-secret-pw -e TZ='America/Lima' -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --log_bin_trust_function_creators=1
```

```
docker exec -u 0 -it sakila-db bash
```

Load the scripts from `database-model/` **in this order**:

| Order | File | Purpose |
|---|---|---|
| 1 | `sakila-schema.sql` | Schema, views, triggers, stored routines (vendored from Oracle) |
| 2 | `sakila-data.sql` | Full sample data (vendored from Oracle) |
| 3 | `auth-fixture.sql` | Widens `staff.password`, adds a unique username index, seeds logins |

## Running

The JWT signing key is read from the environment and has no default, so the app will not
start without it:

```bash
export JWT_SECRET=$(openssl rand -base64 48)
mvn spring-boot:run
```

API on `8181`, actuator on `127.0.0.1:8182`.

### Authenticating

```bash
curl -X POST localhost:8181/auth/token \
  -H 'Content-Type: application/json' \
  -d '{"username":"cesar","password":"cesar"}'
```

```json
{ "accessToken": "eyJ0eXAiOiJKV1Qi...", "tokenType": "Bearer", "expiresIn": 1800 }
```

```bash
curl localhost:8181/actors -H "Authorization: Bearer $TOKEN"
```

Every endpoint except `POST /auth/token` requires a valid token.

Development fixture logins (from `auth-fixture.sql`, local use only):

| Username | Password |
|---|---|
| `Mike` | `12345` |
| `claude` | `claude` |
| `cesar` | `cesar` |

`Jon` also exists in the sample data but has a `NULL` password and therefore cannot log in.

## Tests

```bash
mvn clean verify
```

Tests start a throwaway `mysql:5.7.44` container seeded from `database-model/`, so they
never touch the local development database and cannot drift. Docker must be running; no
other setup is required.

## Inspiration

- [Building REST services with Spring](https://spring.io/guides/tutorials/rest)
