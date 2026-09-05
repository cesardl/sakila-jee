package org.sanmarcux.samples.sakila;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.MountableFile;

/**
 * Base class for tests that need the Sakila database.
 * <p>
 * Every run gets a throwaway MySQL container seeded from {@code database-model/}, so tests
 * never touch -- or drift -- the local development database. Tests mutate data (they create
 * films, customers and film/actor participations), which used to mean the dev database
 * slowly accumulated junk and needed a manual restore.
 * <p>
 * The container is a static field started once per JVM rather than a {@code @Bean}. Spring
 * caches a separate application context per distinct test configuration, and a container
 * bean would be created once per context -- reloading the 3.4MB dump each time. This way
 * every context shares one container. Ryuk tears it down when the JVM exits.
 *
 * @author Cesardl
 */
public abstract class AbstractIntegrationTest {

    // Pinned to MySQL 8, the version the app requires. This is the one piece of the setup
    // that is NOT free to vary: 8.0.2 flipped explicit_defaults_for_timestamp from 0 to 1,
    // so a TIMESTAMP NOT NULL column that 5.7 quietly filled from CURRENT_TIMESTAMP now
    // rejects an explicit NULL. Running these tests on 5.7 hid exactly that bug. Credentials
    // and ports below are Testcontainers' own and deliberately arbitrary.
    private static final MySQLContainer MYSQL = new MySQLContainer("mysql:8.0.46")
            .withDatabaseName("sakila")
            // Same server flags and timezone as the docker line in CLAUDE.md. Connector/J
            // converts TIMESTAMP columns through the connection zone, so tests and local
            // development have to agree or a timestamp bug only shows up in one of them.
            // The schema creates stored functions and triggers, hence
            // log_bin_trust_function_creators.
            .withEnv("TZ", "America/Lima")
            .withCommand(
                    "--character-set-server=utf8mb4",
                    "--collation-server=utf8mb4_unicode_ci",
                    "--log_bin_trust_function_creators=1")
            .withUrlParam("useSSL", "false")
            .withUrlParam("serverTimezone", "America/Lima")
            // The MySQL image runs /docker-entrypoint-initdb.d/*.sql in ALPHABETICAL order,
            // and the natural filenames sort wrong (schema would run last). Hence prefixes.
            .withCopyFileToContainer(
                    MountableFile.forHostPath("database-model/sakila-schema.sql"),
                    "/docker-entrypoint-initdb.d/01-schema.sql")
            .withCopyFileToContainer(
                    MountableFile.forHostPath("database-model/sakila-data.sql"),
                    "/docker-entrypoint-initdb.d/02-data.sql")
            .withCopyFileToContainer(
                    MountableFile.forHostPath("database-model/auth-fixture.sql"),
                    "/docker-entrypoint-initdb.d/03-auth-fixture.sql");

    static {
        MYSQL.start();
    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }
}
