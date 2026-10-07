-- Creates the SELECT-only MySQL account used by query-execution-service.
-- Runs automatically on a FRESH mysql volume (docker-entrypoint-initdb.d) AND on every
-- `docker compose up` via the one-shot `mysql-bootstrap` service, so it must be re-runnable.
--
-- CHANGED: added ALTER USER so the password is always reset to the expected value even if
-- the user already existed from an earlier run.
CREATE USER IF NOT EXISTS 'smm_readonly'@'%' IDENTIFIED BY 'smm_readonly_pass';
ALTER USER 'smm_readonly'@'%' IDENTIFIED BY 'smm_readonly_pass';
FLUSH PRIVILEGES;
