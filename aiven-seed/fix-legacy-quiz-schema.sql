-- One-time cleanup for Aiven: quiz_attempts / quiz_attempt_answers still carry
-- leftover columns and foreign keys from before the Topic/Question migration
-- (back when QuizAttempt had a `quiz` relation and QuizAttemptAnswer had
-- `question`/`selectedOption` relations to the old Quiz/QuizQuestion/QuizOption
-- tables). Hibernate's ddl-auto=update only ADDS columns, it never drops or
-- loosens existing ones, so the old NOT NULL `quiz_id` column is still there
-- and rejects every insert.
--
-- Run this ONCE directly against Aiven, then restart case-content-service.
-- Safe to run even if a given column/constraint was already removed — each
-- block checks information_schema first and does nothing if there's no match.

-- Replace with your actual database name if it isn't defaultdb.
SET @db := 'defaultdb';

-- 1) Drop any foreign key on quiz_attempts.quiz_id, then the column itself.
SET @fk := (
  SELECT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'quiz_attempts'
    AND COLUMN_NAME = 'quiz_id' AND REFERENCED_TABLE_NAME IS NOT NULL
  LIMIT 1
);
SET @sql := IF(@fk IS NOT NULL,
  CONCAT('ALTER TABLE quiz_attempts DROP FOREIGN KEY `', @fk, '`'),
  'SELECT "no FK on quiz_attempts.quiz_id"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @col := (
  SELECT COLUMN_NAME FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'quiz_attempts' AND COLUMN_NAME = 'quiz_id'
  LIMIT 1
);
SET @sql := IF(@col IS NOT NULL,
  'ALTER TABLE quiz_attempts DROP COLUMN quiz_id',
  'SELECT "no quiz_id column on quiz_attempts"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 2) Same for quiz_attempt_answers.question_id — drop any old FK, keep the column.
SET @fk := (
  SELECT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'quiz_attempt_answers'
    AND COLUMN_NAME = 'question_id' AND REFERENCED_TABLE_NAME IS NOT NULL
  LIMIT 1
);
SET @sql := IF(@fk IS NOT NULL,
  CONCAT('ALTER TABLE quiz_attempt_answers DROP FOREIGN KEY `', @fk, '`'),
  'SELECT "no FK on quiz_attempt_answers.question_id"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 3) Same for quiz_attempt_answers.selected_option_id — drop any old FK, keep the column.
SET @fk := (
  SELECT CONSTRAINT_NAME FROM information_schema.KEY_COLUMN_USAGE
  WHERE TABLE_SCHEMA = @db AND TABLE_NAME = 'quiz_attempt_answers'
    AND COLUMN_NAME = 'selected_option_id' AND REFERENCED_TABLE_NAME IS NOT NULL
  LIMIT 1
);
SET @sql := IF(@fk IS NOT NULL,
  CONCAT('ALTER TABLE quiz_attempt_answers DROP FOREIGN KEY `', @fk, '`'),
  'SELECT "no FK on quiz_attempt_answers.selected_option_id"');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Sanity check: quiz_attempts should now have topic_id and no quiz_id.
DESCRIBE quiz_attempts;
DESCRIBE quiz_attempt_answers;
