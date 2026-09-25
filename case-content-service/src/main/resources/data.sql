-- Learning Phase seed data.
-- Runs after Hibernate creates the schema (defer-datasource-initialization: true,
-- sql.init.mode: always in application.yml). Safe to re-run: each block deletes its
-- own rows first so restarts don't duplicate quizzes.

DELETE FROM quiz_attempt_answers;
DELETE FROM quiz_attempts;
DELETE FROM quiz_options;
DELETE FROM quiz_questions;
DELETE FROM quizzes;

-- ============================================================
-- Topic 1: SELECT Basics
-- ============================================================
INSERT INTO quizzes (id, topic, description, difficulty, time_limit_seconds, display_order) VALUES
  (1, 'SELECT Basics', 'Reading data out of a single table.', 'BEGINNER', 180, 1);

INSERT INTO quiz_questions (id, quiz_id, question_text, code_snippet, points) VALUES
  (101, 1, 'Which statement returns every column for every row in the "suspects" table?', NULL, 10),
  (102, 1, 'What does the DISTINCT keyword do?', 'SELECT DISTINCT city FROM suspects;', 10),
  (103, 1, 'Which clause limits the number of rows returned?', 'SELECT * FROM suspects LIMIT 5;', 10);

INSERT INTO quiz_options (question_id, option_text, is_correct) VALUES
  (101, 'SELECT * FROM suspects;', TRUE),
  (101, 'SELECT ALL suspects;', FALSE),
  (101, 'GET * FROM suspects;', FALSE),
  (101, 'FETCH suspects.*;', FALSE),
  (102, 'Removes duplicate rows from the result', TRUE),
  (102, 'Sorts the result alphabetically', FALSE),
  (102, 'Deletes duplicate rows from the table', FALSE),
  (102, 'Counts distinct rows', FALSE),
  (103, 'LIMIT', TRUE),
  (103, 'TOP', FALSE),
  (103, 'ROWCOUNT', FALSE),
  (103, 'MAX ROWS', FALSE);

-- ============================================================
-- Topic 2: WHERE & Filtering
-- ============================================================
INSERT INTO quizzes (id, topic, description, difficulty, time_limit_seconds, display_order) VALUES
  (2, 'WHERE & Filtering', 'Narrowing results down with conditions.', 'BEGINNER', 180, 2);

INSERT INTO quiz_questions (id, quiz_id, question_text, code_snippet, points) VALUES
  (201, 2, 'Which query finds suspects whose height is between 160 and 180 cm?', NULL, 10),
  (202, 2, 'What does this query return?', 'SELECT * FROM person WHERE name LIKE ''J%'';', 10),
  (203, 2, 'Which operator checks for NULL correctly?', NULL, 10);

INSERT INTO quiz_options (question_id, option_text, is_correct) VALUES
  (201, 'SELECT * FROM suspects WHERE height BETWEEN 160 AND 180;', TRUE),
  (201, 'SELECT * FROM suspects WHERE height IN (160, 180);', FALSE),
  (201, 'SELECT * FROM suspects WHERE height = 160 TO 180;', FALSE),
  (201, 'SELECT * FROM suspects RANGE height 160 180;', FALSE),
  (202, 'People whose name starts with "J"', TRUE),
  (202, 'People whose name contains "J" anywhere', FALSE),
  (202, 'People whose name ends with "J"', FALSE),
  (202, 'People whose name is exactly "J"', FALSE),
  (203, 'IS NULL', TRUE),
  (203, '= NULL', FALSE),
  (203, '== NULL', FALSE),
  (203, 'LIKE NULL', FALSE);

-- ============================================================
-- Topic 3: JOINs
-- ============================================================
INSERT INTO quizzes (id, topic, description, difficulty, time_limit_seconds, display_order) VALUES
  (3, 'JOINs', 'Combining rows across related tables.', 'INTERMEDIATE', 240, 3);

INSERT INTO quiz_questions (id, quiz_id, question_text, code_snippet, points) VALUES
  (301, 3, 'Which JOIN returns only rows that match in both tables?', NULL, 10),
  (302, 3, 'Which JOIN keeps every row from the left table, even with no match?', NULL, 10),
  (303, 3, 'What is wrong with this query?', 'SELECT * FROM person, security_log WHERE person.id = security_log.person_id;', 10);

INSERT INTO quiz_options (question_id, option_text, is_correct) VALUES
  (301, 'INNER JOIN', TRUE),
  (301, 'LEFT JOIN', FALSE),
  (301, 'CROSS JOIN', FALSE),
  (301, 'FULL OUTER JOIN', FALSE),
  (302, 'LEFT JOIN', TRUE),
  (302, 'INNER JOIN', FALSE),
  (302, 'RIGHT JOIN', FALSE),
  (302, 'CROSS JOIN', FALSE),
  (303, 'Nothing — it is an old-style implicit INNER JOIN and works fine', TRUE),
  (303, 'It will always error out', FALSE),
  (303, 'Implicit joins are not valid SQL', FALSE),
  (303, 'It performs a LEFT JOIN automatically', FALSE);

-- ============================================================
-- Topic 4: GROUP BY & Aggregates
-- ============================================================
INSERT INTO quizzes (id, topic, description, difficulty, time_limit_seconds, display_order) VALUES
  (4, 'GROUP BY & Aggregates', 'Summarizing rows with COUNT, SUM, AVG and friends.', 'INTERMEDIATE', 240, 4);

INSERT INTO quiz_questions (id, quiz_id, question_text, code_snippet, points) VALUES
  (401, 4, 'Which clause filters groups AFTER aggregation?', 'SELECT city, COUNT(*) FROM person GROUP BY city ___ COUNT(*) > 2;', 10),
  (402, 4, 'Why does this query fail?', 'SELECT city, name, COUNT(*) FROM person GROUP BY city;', 10),
  (403, 4, 'Which function counts non-NULL values in a column?', NULL, 10);

INSERT INTO quiz_options (question_id, option_text, is_correct) VALUES
  (401, 'HAVING', TRUE),
  (401, 'WHERE', FALSE),
  (401, 'FILTER', FALSE),
  (401, 'QUALIFY', FALSE),
  (402, '"name" is not aggregated and not in the GROUP BY list', TRUE),
  (402, 'COUNT(*) cannot be used with GROUP BY', FALSE),
  (402, 'GROUP BY requires an ORDER BY', FALSE),
  (402, 'City must be numeric to group by', FALSE),
  (403, 'COUNT(column_name)', TRUE),
  (403, 'SUM(column_name)', FALSE),
  (403, 'COUNT(*)', FALSE),
  (403, 'TOTAL(column_name)', FALSE);

-- ============================================================
-- Case-Solving Phase — case metadata.
-- The actual explorable data lives in the standalone MySQL schemas created by
-- mysql-init/02-case-gallery-theft.sql and 03-case-archive-heist.sql, which
-- query-execution-service (not this service) connects to.
-- ============================================================
DELETE FROM case_files;

INSERT INTO case_files (id, title, briefing, difficulty, target_schema, solution_suspect_id, solution_explanation, points_reward, display_order) VALUES
(1,
 'The Gallery After Dark',
 'On the night of March 14th, the prized painting "Moonlit Harbor" vanished from the Lindgren Gallery. The rear emergency door was forced open, and a single torn strip of red fabric was left on the frame. Query the crime_scene_report, person, witness_statement and security_log tables to figure out who was in the Back Corridor when the theft occurred.',
 'BEGINNER',
 'case_gallery_theft',
 3,
 'Tomas Rivera, the HVAC contractor, was the only person badged into the Back Corridor during the theft window (9:40-10:05 PM), matching both the timing of the break-in and witness reports of a maintenance jumpsuit near the scene.',
 100,
 1),
(2,
 'The Vanishing Manuscript',
 'The 15th-century "Codex of Ashford" has disappeared from Archive Vault B. There was no forced entry to the building, so the thief had a valid key card. A witness noticed unusually frequent late-night visits to the vault in the week before the theft. Use GROUP BY and COUNT on the security_log table to find who visited far more often than anyone else.',
 'INTERMEDIATE',
 'case_archive_heist',
 2,
 'Robert Nakashima badged into Vault B seven times in the week before the theft, far more than any colleague, despite having no active cataloguing project assigned there.',
 150,
 2);
