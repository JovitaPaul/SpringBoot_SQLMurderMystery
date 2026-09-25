CREATE DATABASE IF NOT EXISTS case_archive_heist;
USE case_archive_heist;

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
    (1, 'Helena Fitzgerald', 'Head Archivist',        '555-0211'),
    (2, 'Robert Nakashima',  'Junior Archivist',      '555-0234'),
    (3, 'Clara Studham',     'Facilities Manager',    '555-0245'),
    (4, 'Youssef Amrani',    'Visiting Researcher',   '555-0256'),
    (5, 'Betty Coleman',     'Security Officer',      '555-0267'),
    (6, 'Liam Docherty',     'IT Technician',         '555-0278');

INSERT INTO crime_scene_report (id, occurred_at, location, description) VALUES
    (1, '2025-04-02 23:10:00', 'Archive Vault B, University Library',
     'The climate-controlled case in Vault B was forced open and the 15th-century "Codex of Ashford" is missing. There is no sign of forced entry to the building itself, so whoever took it had a valid vault key card.');

INSERT INTO witness_statement (id, witness_person_id, statement) VALUES
    (1, 5, 'I noticed on the access log that someone from Special Collections staff had been badging into Vault B almost every night the week before, way more than is normal for routine handling.'),
    (2, 3, 'I saw Robert near the vault late one night and he said he was "cataloguing a difficult acquisition" — but the Codex wasn''t part of any active cataloguing project I knew about.');

-- Multiple visits per person to Vault B across the week leading up to the theft.
INSERT INTO security_log (id, person_id, area, entry_time, exit_time) VALUES
    (1,  1, 'Vault B', '2025-03-27 10:00:00', '2025-03-27 11:00:00'),
    (2,  1, 'Vault B', '2025-03-30 14:00:00', '2025-03-30 15:00:00'),
    (3,  2, 'Vault B', '2025-03-26 22:40:00', '2025-03-26 23:20:00'),
    (4,  2, 'Vault B', '2025-03-27 22:50:00', '2025-03-27 23:30:00'),
    (5,  2, 'Vault B', '2025-03-28 22:35:00', '2025-03-28 23:15:00'),
    (6,  2, 'Vault B', '2025-03-29 22:55:00', '2025-03-29 23:40:00'),
    (7,  2, 'Vault B', '2025-03-30 23:05:00', '2025-03-30 23:45:00'),
    (8,  2, 'Vault B', '2025-03-31 22:48:00', '2025-03-31 23:25:00'),
    (9,  2, 'Vault B', '2025-04-01 22:52:00', '2025-04-01 23:30:00'),
    (10, 3, 'Vault B', '2025-03-28 09:00:00', '2025-03-28 09:30:00'),
    (11, 4, 'Reading Room', '2025-03-27 10:00:00', '2025-03-27 16:00:00'),
    (12, 5, 'Main Floor', '2025-03-27 18:00:00', '2025-04-02 06:00:00'),
    (13, 6, 'Vault B', '2025-03-29 13:00:00', '2025-03-29 13:20:00');

GRANT SELECT ON case_archive_heist.* TO 'smm_readonly'@'%';
