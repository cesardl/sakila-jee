# Sakila Sample Application [![Build Status](https://travis-ci.org/cesardl/sakila-jee.svg?branch=master)](https://travis-ci.org/cesardl/sakila-jee) [![Maintainability](https://api.codeclimate.com/v1/badges/77118783b8939faf1de2/maintainability)](https://codeclimate.com/github/cesardl/sakila-jee/maintainability) <a href="https://codeclimate.com/github/cesardl/sakila-jee/test_coverage"><img src="https://api.codeclimate.com/v1/badges/77118783b8939faf1de2/test_coverage" /></a>

Demo with Spring Boot and Sakila sample database from MySQL

```
docker run --name sakila-db -p 3314:3306 --restart on-failure -e MYSQL_DATABASE=sakila -e MYSQL_ROOT_PASSWORD=rootroot -e MYSQL_USER=travis -e MYSQL_PASSWORD=my-secret-pw -e TZ='America/Lima' -d mysql --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --log_bin_trust_function_creators=1
```

```
docker exec -u 0 -it sakila-db bash
```

Inspiration

- [Building REST services with Spring](https://spring.io/guides/tutorials/rest)
