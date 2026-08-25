-- Authentication fixture for sakila-jee.
--
-- Runs AFTER sakila-schema.sql and sakila-data.sql, which are vendored from Oracle and
-- deliberately left untouched. Everything referenced below -- address 3 and 4, store 1
-- and 2, staff 'Mike' -- already exists by the time this runs, so there is no bootstrap
-- chain and none of the FOREIGN_KEY_CHECKS juggling the vendored files need.
--
-- This replaces the old data.sql, which was a Travis-era extraction (language 1, actors
-- 100/101, films 1-3) built to avoid shipping the 3.4MB dump to a CI container. Those
-- rows now collide with the full dump on duplicate keys.
--
-- The two ALTER statements are one-time DDL: re-running this file on an already-migrated
-- database reports "Duplicate key name", which is harmless and can be ignored. The
-- UPDATE and INSERT below are idempotent.

-- Sakila sizes this column for a 40-char SHA-1 hex digest. Spring Security's
-- DelegatingPasswordEncoder stores an algorithm prefix followed by the hash:
--   {bcrypt} = 68 chars, {argon2} ~= 123, {scrypt} can exceed 200.
-- 255 means a later move to argon2 is a rehash-on-login, not a schema migration.
ALTER TABLE staff MODIFY password VARCHAR(255) BINARY DEFAULT NULL;

-- StaffRepository.findByUsername returns Optional<Staff>, so two staff rows sharing a
-- username would throw IncorrectResultSizeDataAccessException from inside the login
-- path. Stock Sakila ships no such constraint.
ALTER TABLE staff ADD UNIQUE KEY idx_unique_username (username);

-- 'Mike' already exists carrying Sakila's unsalted SHA-1 of "12345". Re-hash in place
-- rather than re-inserting the row.
UPDATE staff
SET password = '{bcrypt}$2a$10$duF6mpctUV0jSGZvos6sG.siWhst1pAhjz.kSHWBcaXQqgjfiuJAi'
WHERE username = 'Mike';

-- Two extra logins. staff_id is AUTO_INCREMENT so no ids are forced. INSERT IGNORE plus
-- the unique index above makes re-running this a no-op on a long-lived dev database.
--
-- Dev fixture credentials, never intended to leave a local machine:
--   Mike   / 12345
--   claude / claude
--   cesar  / cesar
INSERT IGNORE INTO staff (first_name, last_name, address_id, email, store_id, active, username, password)
VALUES ('Claude', 'Sonnet', 3, 'claude@sakilastaff.com', 1, 1, 'claude',
        '{bcrypt}$2a$10$nZxKYL49tRMUjwmOeoJI0OmnHPMzNm1bO5cyWgJdQfVkp/3FWSeDu'),
       ('Cesar', 'Diaz', 4, 'cesar@sakilastaff.com', 2, 1, 'cesar',
        '{bcrypt}$2a$10$CtbOHh9VbqO.P9G/gh3UNeNU8JRAfHBMuuGWvoMS6zdOGPBm58tCq');
