-- Case metadata is local application metadata.
-- Learning questions are NOT seeded here; they are loaded from Aiven's
-- `topics` and `questions` tables by QuizService.

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
