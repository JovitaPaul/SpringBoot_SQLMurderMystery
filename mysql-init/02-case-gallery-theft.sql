CREATE DATABASE IF NOT EXISTS case_gallery_theft;
USE case_gallery_theft;

CREATE TABLE person (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    occupation VARCHAR(100),
    phone_number VARCHAR(20)
);

CREATE TABLE crime_scene_report (
    id INT PRIMARY KEY,
    occurred_at DATETIME NOT NULL,
    location VARCHAR(150) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE witness_statement (
    id INT PRIMARY KEY,
    witness_person_id INT NOT NULL,
    statement TEXT NOT NULL,
    FOREIGN KEY (witness_person_id) REFERENCES person(id)
);

CREATE TABLE security_log (
    id INT PRIMARY KEY,
    person_id INT NOT NULL,
    area VARCHAR(100) NOT NULL,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person(id)
);

INSERT INTO person (id, name, occupation, phone_number) VALUES
    (1, 'Marcus Boyle',       'Night Security Guard',   '555-0142'),
    (2, 'Elena Vasquez',      'Gallery Curator',        '555-0198'),
    (3, 'Tomas Rivera',       'HVAC Contractor',        '555-0177'),
    (4, 'Priya Chandrasekar', 'Visiting Art Student',   '555-0133'),
    (5, 'Sam O''Neal',        'Gallery Cafe Barista',   '555-0119'),
    (6, 'Ingrid Fossum',      'Museum Board Member',    '555-0166'),
    (7, 'Dana Whitlock',      'Freelance Photographer', '555-0155'),
    (8, 'Owen Bright',        'Parking Valet',          '555-0188');

INSERT INTO crime_scene_report (id, occurred_at, location, description) VALUES
    (1, '2025-03-14 21:47:00', 'Back Corridor, Lindgren Gallery',
     'Rear emergency door forced open from outside; broken window latch; a single torn strip of red fabric caught on the frame. The painting "Moonlit Harbor" is missing from Hall 3.');

INSERT INTO witness_statement (id, witness_person_id, statement) VALUES
    (1, 5, 'Around 9:50pm I saw someone in a maintenance jumpsuit hurry out the back with a rolled-up tube. Didn''t see a face, but there was a flash of red near the collar.'),
    (2, 6, 'I was in the lobby until 9:30 and didn''t notice anything odd, but I did see a contractor''s van still parked in the service alley later than I''d have expected.'),
    (3, 4, 'I left the gift shop about 9:15. The HVAC contractor''s toolbox was propped open near the Back Corridor door, which struck me as unusual since they normally close up by 8.');

INSERT INTO security_log (id, person_id, area, entry_time, exit_time) VALUES
    (1, 1, 'Main Floor',    '2025-03-14 18:00:00', '2025-03-14 23:00:00'),
    (2, 2, 'Curator Office','2025-03-14 17:00:00', '2025-03-14 20:45:00'),
    (3, 3, 'Back Corridor', '2025-03-14 21:40:00', '2025-03-14 22:05:00'),
    (4, 4, 'Gift Shop',     '2025-03-14 18:30:00', '2025-03-14 21:15:00'),
    (5, 5, 'Cafe',          '2025-03-14 19:00:00', '2025-03-14 22:00:00'),
    (6, 6, 'Lobby',         '2025-03-14 18:00:00', '2025-03-14 21:30:00'),
    (7, 7, 'Hall 3',        '2025-03-14 19:30:00', '2025-03-14 21:00:00'),
    (8, 8, 'Parking Lot',   '2025-03-14 18:00:00', '2025-03-14 22:30:00');

GRANT SELECT ON case_gallery_theft.* TO 'smm_readonly'@'%';
