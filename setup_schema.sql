-- Run this FIRST, then new_20_cases.sql
-- Only creates the crime-mystery tables; your existing quiz/user tables are not touched.

CREATE TABLE IF NOT EXISTS crime_scene_report (
    case_id INTEGER PRIMARY KEY,
    date DATE,
    type VARCHAR(100) NOT NULL,
    description TEXT,
    city VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS drivers_license (
    id INT PRIMARY KEY,
    age INT,
    height FLOAT,
    eye_color VARCHAR(20),
    hair_color VARCHAR(50),
    gender VARCHAR(10),
    plate_number VARCHAR(10),
    car_make VARCHAR(50),
    car_model VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS person (
    person_id INTEGER PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    license_id INTEGER,
    city VARCHAR(100),
    FOREIGN KEY (license_id) REFERENCES drivers_license(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS interview (
    case_id INT,
    person_id INT,
    role VARCHAR(50),
    description VARCHAR(255),
    PRIMARY KEY (case_id, person_id),
    FOREIGN KEY (case_id) REFERENCES crime_scene_report(case_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (person_id) REFERENCES person(person_id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

-- date changed from DATE to DATETIME so the check-in times are kept
CREATE TABLE IF NOT EXISTS facebook_event_checkin (
    event_id INT PRIMARY KEY,
    person_id INTEGER,
    event_description TEXT,
    date DATETIME,
    FOREIGN KEY (person_id) REFERENCES person(person_id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS relationships (
    relationship_id INT PRIMARY KEY,
    person1_id INTEGER,
    person2_id INTEGER,
    relation VARCHAR(100),
    FOREIGN KEY (person1_id) REFERENCES person(person_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (person2_id) REFERENCES person(person_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS phone_records (
    record_id INT PRIMARY KEY,
    case_id INT,
    caller_id INT,
    receiver_id INT,
    call_duration_mins INT,
    description VARCHAR(255),
    FOREIGN KEY (caller_id) REFERENCES person(person_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES person(person_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (case_id) REFERENCES crime_scene_report(case_id)
    ON DELETE CASCADE ON UPDATE CASCADE
);
