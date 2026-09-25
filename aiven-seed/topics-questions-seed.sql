-- One-time seed for Aiven's `topics` and `questions` tables.
--
-- case-content-service no longer seeds quiz content itself (see case-data.sql and
-- QuizService) — it reads these two tables live from Aiven on every request.
-- Run this ONCE directly against your Aiven MySQL instance to populate them.
-- Safe to re-run: it deletes its own rows first.
--
-- Example, using the values from your .env file:
--   mysql --host=mysql-1e392661-nitk-390.i.aivencloud.com --port=12751 \
--         -u avnadmin -p defaultdb \
--         --ssl-mode=REQUIRED < topics-questions-seed.sql
--
-- Hibernate (ddl-auto: update) will create these tables automatically the first
-- time case-content-service starts, if they don't already exist. If it hasn't
-- run yet, start the service once first, then run this script.

DELETE FROM questions;
DELETE FROM topics;

INSERT INTO topics (id, name) VALUES
  (1, 'SELECT Basics'),
  (2, 'WHERE & Filtering'),
  (3, 'JOINs'),
  (4, 'GROUP BY & Aggregates');

-- Topic 1: SELECT Basics
INSERT INTO questions (id, topic_id, question, option1, option2, option3, option4, correct_option) VALUES
(101, 1, 'Which statement returns every column for every row in the "suspects" table?',
  'GET * FROM suspects;', 'SELECT * FROM suspects;', 'FETCH suspects.*;', 'SELECT ALL suspects;', 2),
(102, 1, 'What does the DISTINCT keyword do? (SELECT DISTINCT city FROM suspects;)',
  'Sorts the result alphabetically', 'Counts distinct rows',
  'Removes duplicate rows from the result', 'Deletes duplicate rows from the table', 3),
(103, 1, 'Which clause limits the number of rows returned? (SELECT * FROM suspects LIMIT 5;)',
  'TOP', 'MAX ROWS', 'ROWCOUNT', 'LIMIT', 4);

-- Topic 2: WHERE & Filtering
INSERT INTO questions (id, topic_id, question, option1, option2, option3, option4, correct_option) VALUES
(201, 2, 'Which query finds suspects whose height is between 160 and 180 cm?',
  'SELECT * FROM suspects WHERE height IN (160, 180);',
  'SELECT * FROM suspects WHERE height BETWEEN 160 AND 180;',
  'SELECT * FROM suspects WHERE height = 160 TO 180;',
  'SELECT * FROM suspects RANGE height 160 180;', 2),
(202, 2, 'What does this query return? (SELECT * FROM person WHERE name LIKE ''J%'';)',
  'People whose name is exactly "J"', 'People whose name ends with "J"',
  'People whose name starts with "J"', 'People whose name contains "J" anywhere', 3),
(203, 2, 'Which operator checks for NULL correctly?',
  '= NULL', 'IS NULL', '== NULL', 'LIKE NULL', 2);

-- Topic 3: JOINs
INSERT INTO questions (id, topic_id, question, option1, option2, option3, option4, correct_option) VALUES
(301, 3, 'Which JOIN returns only rows that match in both tables?',
  'FULL OUTER JOIN', 'LEFT JOIN', 'INNER JOIN', 'CROSS JOIN', 3),
(302, 3, 'Which JOIN keeps every row from the left table, even with no match?',
  'LEFT JOIN', 'INNER JOIN', 'RIGHT JOIN', 'CROSS JOIN', 1),
(303, 3, 'What is wrong with this query? (SELECT * FROM person, security_log WHERE person.id = security_log.person_id;)',
  'It will always error out', 'It performs a LEFT JOIN automatically',
  'Implicit joins are not valid SQL',
  'Nothing — it is an old-style implicit INNER JOIN and works fine', 4);

-- Topic 4: GROUP BY & Aggregates
INSERT INTO questions (id, topic_id, question, option1, option2, option3, option4, correct_option) VALUES
(401, 4, 'Which clause filters groups AFTER aggregation? (SELECT city, COUNT(*) FROM person GROUP BY city ___ COUNT(*) > 2;)',
  'WHERE', 'HAVING', 'FILTER', 'QUALIFY', 2),
(402, 4, 'Why does this query fail? (SELECT city, name, COUNT(*) FROM person GROUP BY city;)',
  'COUNT(*) cannot be used with GROUP BY', '"name" is not aggregated and not in the GROUP BY list',
  'GROUP BY requires an ORDER BY', 'City must be numeric to group by', 2),
(403, 4, 'Which function counts non-NULL values in a column?',
  'SUM(column_name)', 'TOTAL(column_name)', 'COUNT(column_name)', 'COUNT(*)', 3);
