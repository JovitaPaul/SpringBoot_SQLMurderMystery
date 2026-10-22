-- Run ONCE as the admin user (avnadmin) against the Aiven `defaultdb` database, e.g.:
--   mysql -h <aiven-host> -P <port> -u avnadmin -p --ssl-mode=REQUIRED defaultdb < 05-readonly-grants-defaultdb.sql
-- (or paste it into Aiven's web SQL console / MySQL Workbench).
--
-- Gives the query-execution account SELECT on the learner-visible case tables ONLY.
-- Because there is no grant on `solution` (or on users/quiz/etc.), MySQL itself
-- refuses to read it, and SHOW TABLES / SHOW COLUMNS never list it.
-- NOTE: SHOW and DESCRIBE need no extra privilege beyond SELECT on the table.

CREATE USER IF NOT EXISTS 'smm_readonly'@'%' IDENTIFIED BY 'smm_readonly_pass';
-- Change the password for anything beyond local dev, and keep it in sync with
-- QUERY_DB_READONLY_PASSWORD in .env.

-- Start clean so re-running is safe and an old broad grant can't linger.
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'smm_readonly'@'%';

GRANT SELECT ON defaultdb.crime_scene_report     TO 'smm_readonly'@'%';
GRANT SELECT ON defaultdb.drivers_license        TO 'smm_readonly'@'%';
GRANT SELECT ON defaultdb.person                 TO 'smm_readonly'@'%';
GRANT SELECT ON defaultdb.interview              TO 'smm_readonly'@'%';
GRANT SELECT ON defaultdb.facebook_event_checkin TO 'smm_readonly'@'%';
GRANT SELECT ON defaultdb.relationships          TO 'smm_readonly'@'%';
GRANT SELECT ON defaultdb.phone_records          TO 'smm_readonly'@'%';
-- Deliberately NOT granted: defaultdb.solution

FLUSH PRIVILEGES;

-- Verify:
-- SHOW GRANTS FOR 'smm_readonly'@'%';
