-- Runs once, automatically, the first time the mysql container starts on a fresh
-- volume (docker-entrypoint-initdb.d convention — files run in filename order).
-- If you already have a mysql_data volume from before, either `docker compose down -v`
-- to recreate it, or run these files by hand with `docker exec -it smm-mysql mysql -uroot -proot`.

-- A dedicated MySQL account with SELECT-only privileges. query-execution-service
-- connects with this account, so even if the SQL-safety validator in the application
-- layer had a bug, the database itself refuses any write/DDL statement. This is the
-- "enforcing read-only access... through careful service design" defense the proposal
-- calls for — belt (query validation) and suspenders (DB grants).
CREATE USER IF NOT EXISTS 'smm_readonly'@'%' IDENTIFIED BY 'smm_readonly_pass';
