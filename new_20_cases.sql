-- ===== 20 new cases (3002-3021). ID ranges: person 4010+, license 5010+, event 6110+, phone 8110+, relationship 9110+ =====
CREATE TABLE IF NOT EXISTS solution (
    case_id INTEGER PRIMARY KEY,
    culprit_person_id INTEGER,
    motive TEXT,
    key_evidence TEXT,
    FOREIGN KEY (case_id) REFERENCES crime_scene_report(case_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (culprit_person_id) REFERENCES person(person_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- NOTE: your pasted data was cut off before case 3001's answer, so no solution row is inserted for 3001 here.

-- ---------- Case 3002: Burglary in Riverdale ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3002, '2024-01-14', 'Burglary', 'Burglary at Harbor Jewelers. Display cases emptied, back door unlocked with a key. Alarm code used was valid. Incident window around 23:00-00:30. Witness saw a green Toyota leaving the scene.', 'Riverdale');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5010, 56, 168.0, 'brown', 'red', 'female', 'BJ-1037', 'Honda', 'Civic'),
(5011, 29, 182.0, 'hazel', 'black', 'male', 'AD-1148', 'Toyota', 'Camry'),
(5012, 28, 170.0, 'brown', 'blonde', 'female', 'LL-1259', 'Toyota', 'Sedan'),
(5013, 59, 182.0, 'brown', 'red', 'male', 'QM-1370', 'Toyota', 'Corolla'),
(5014, 60, 162.0, 'blue', 'black', 'male', 'VN-1481', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4010, 'Bella Hayes', 'Riverdale', 5010),
(4011, 'Caleb Owens', 'Riverdale', 5011),
(4012, 'Diana Vance', 'Riverdale', 5012),
(4013, 'Edwin Doyle', 'Riverdale', 5013),
(4014, 'Felix Knox', 'Springfield', 5014);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3002, 4013, 'Witness', 'Saw a green Toyota leave fast around 23:15. Partial plate began with ''AD''. Driver had black hair and looked male.'),
(3002, 4011, 'Person of Interest', 'Denies involvement. Says he was at Bowling alley all evening and has had no contact with Bella Hayes recently.'),
(3002, 4012, 'Person of Interest', 'Admits to a recent argument with Bella Hayes but says she was at a public event all night. Drives a Toyota.'),
(3002, 4014, 'Associate', 'Says Caleb Owens is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6120, 4012, 'Public event with friends - photo tagged', '2024-01-14 22:40:00'),
(6121, 4014, 'Dinner at home with family', '2024-01-14 21:00:00'),
(6122, 4011, 'Checked in: Bowling alley (early evening)', '2024-01-14 19:30:00'),
(6123, 4012, 'Posted photo from the same event, late', '2024-01-14 00:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8120, 3002, 4012, 4010, 9, 'Heated call at 18:10, argument about money'),
(8121, 3002, 4011, 4010, 4, 'Call at 22:20, short and tense, 40 minutes before incident'),
(8122, 3002, 4011, 4014, 6, 'Call at 00:05, right after incident; location pinged near the scene'),
(8123, 3002, 4014, 4011, 2, 'Call at 01:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9120, 4010, 4011, 'Former employee'),
(9121, 4010, 4012, 'Acquaintance with prior dispute'),
(9122, 4011, 4014, 'Cousins'),
(9123, 4010, 4013, 'Neighbor');

-- ---------- Case 3003: Homicide in Oakridge ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3003, '2024-01-29', 'Homicide', 'Homicide at Maple Street apartment. Victim found in living room, struck from behind. Door was not forced. Incident window around 22:00-23:30. Witness saw a blue Honda leaving the scene.', 'Oakridge');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5020, 31, 174.0, 'blue', 'brown', 'male', 'CJ-1074', 'Honda', 'Civic'),
(5021, 30, 167.0, 'green', 'blonde', 'female', 'BG-1185', 'Honda', 'Accord'),
(5022, 30, 190.0, 'brown', 'gray', 'male', 'MN-1296', 'Honda', 'Sedan'),
(5023, 60, 158.0, 'blue', 'brown', 'male', 'RP-1407', 'Toyota', 'Corolla'),
(5024, 55, 189.0, 'hazel', 'red', 'male', 'WR-1518', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4020, 'Gordon Carter', 'Oakridge', 5020),
(4021, 'Hana Jensen', 'Oakridge', 5021),
(4022, 'Ivan Quigley', 'Oakridge', 5022),
(4023, 'Jonas Yates', 'Oakridge', 5023),
(4024, 'Kevin Frost', 'Springfield', 5024);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3003, 4023, 'Witness', 'Saw a blue Honda leave fast around 22:15. Partial plate began with ''BG''. Driver had blonde hair and looked female.'),
(3003, 4021, 'Person of Interest', 'Denies involvement. Says she was at Late shift at gym all evening and has had no contact with Gordon Carter recently.'),
(3003, 4022, 'Person of Interest', 'Admits to a recent argument with Gordon Carter but says he was at a public event all night. Drives a Honda.'),
(3003, 4024, 'Associate', 'Says Hana Jensen is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6130, 4022, 'Public event with friends - photo tagged', '2024-01-29 21:40:00'),
(6131, 4024, 'Dinner at home with family', '2024-01-29 20:00:00'),
(6132, 4021, 'Checked in: Late shift at gym (early evening)', '2024-01-29 18:30:00'),
(6133, 4022, 'Posted photo from the same event, late', '2024-01-29 23:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8130, 3003, 4022, 4020, 9, 'Heated call at 17:10, argument about money'),
(8131, 3003, 4021, 4020, 4, 'Call at 21:20, short and tense, 40 minutes before incident'),
(8132, 3003, 4021, 4024, 6, 'Call at 23:05, right after incident; location pinged near the scene'),
(8133, 3003, 4024, 4021, 2, 'Call at 00:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9130, 4020, 4021, 'Tenant facing eviction'),
(9131, 4020, 4022, 'Acquaintance with prior dispute'),
(9132, 4021, 4024, 'Cousins'),
(9133, 4020, 4023, 'Neighbor');

-- ---------- Case 3004: Arson in Springfield ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3004, '2024-02-10', 'Arson', 'Arson at Greenfield Warehouse. Fire started near stored inventory using accelerant. Insurance policy raised last month. Incident window around 02:00-03:30. Witness saw a white Ford leaving the scene.', 'Springfield');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5030, 57, 186.0, 'green', 'brown', 'female', 'DJ-1111', 'Honda', 'Civic'),
(5031, 52, 173.0, 'brown', 'brown', 'female', 'CJ-1222', 'Ford', 'Focus'),
(5032, 31, 187.0, 'hazel', 'black', 'female', 'NP-1333', 'Ford', 'Sedan'),
(5033, 34, 176.0, 'blue', 'red', 'female', 'SS-1444', 'Toyota', 'Corolla'),
(5034, 55, 181.0, 'brown', 'blonde', 'female', 'XV-1555', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4030, 'Laura Moss', 'Springfield', 5030),
(4031, 'Maya Ellis', 'Springfield', 5031),
(4032, 'Nina Lowe', 'Springfield', 5032),
(4033, 'Opal Stone', 'Springfield', 5033),
(4034, 'Priya Archer', 'Oakridge', 5034);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3004, 4033, 'Witness', 'Saw a white Ford leave fast around 02:15. Partial plate began with ''CJ''. Driver had brown hair and looked female.'),
(3004, 4031, 'Person of Interest', 'Denies involvement. Says she was at Cousin''s house party all evening and has had no contact with Laura Moss recently.'),
(3004, 4032, 'Person of Interest', 'Admits to a recent argument with Laura Moss but says she was at a public event all night. Drives a Ford.'),
(3004, 4034, 'Associate', 'Says Maya Ellis is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6140, 4032, 'Public event with friends - photo tagged', '2024-02-10 01:40:00'),
(6141, 4034, 'Dinner at home with family', '2024-02-10 00:00:00'),
(6142, 4031, 'Checked in: Cousin''s house party (early evening)', '2024-02-10 22:30:00'),
(6143, 4032, 'Posted photo from the same event, late', '2024-02-10 03:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8140, 3004, 4032, 4030, 9, 'Heated call at -3:10, argument about money'),
(8141, 3004, 4031, 4030, 4, 'Call at 01:20, short and tense, 40 minutes before incident'),
(8142, 3004, 4031, 4034, 6, 'Call at 03:05, right after incident; location pinged near the scene'),
(8143, 3004, 4034, 4031, 2, 'Call at 04:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9140, 4030, 4031, 'Business partner'),
(9141, 4030, 4032, 'Acquaintance with prior dispute'),
(9142, 4031, 4034, 'Cousins'),
(9143, 4030, 4033, 'Neighbor');

-- ---------- Case 3005: Fraud in Riverdale ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3005, '2024-02-21', 'Fraud', 'Fraud at Bright Future Charity. Donations rerouted to a shell account over six weeks. Incident window around 14:00-15:30. Witness saw a blue BMW leaving the scene.', 'Riverdale');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5040, 28, 158.0, 'green', 'gray', 'male', 'EJ-1148', 'Honda', 'Civic'),
(5041, 60, 183.0, 'green', 'black', 'female', 'DM-1259', 'BMW', 'X3'),
(5042, 48, 177.0, 'brown', 'black', 'female', 'OR-1370', 'BMW', 'Sedan'),
(5043, 53, 177.0, 'blue', 'blonde', 'female', 'TV-1481', 'Toyota', 'Corolla'),
(5044, 31, 186.0, 'brown', 'gray', 'female', 'YZ-1592', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4040, 'Quentin Holt', 'Riverdale', 5040),
(4041, 'Rita Oakes', 'Riverdale', 5041),
(4042, 'Sara Grant', 'Riverdale', 5042),
(4043, 'Tessa Nash', 'Riverdale', 5043),
(4044, 'Alice Upton', 'Springfield', 5044);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3005, 4043, 'Witness', 'Saw a blue BMW leave fast around 14:15. Partial plate began with ''DM''. Driver had black hair and looked female.'),
(3005, 4041, 'Person of Interest', 'Denies involvement. Says she was at Online webinar all evening and has had no contact with Quentin Holt recently.'),
(3005, 4042, 'Person of Interest', 'Admits to a recent argument with Quentin Holt but says she was at a public event all night. Drives a BMW.'),
(3005, 4044, 'Associate', 'Says Rita Oakes is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6150, 4042, 'Public event with friends - photo tagged', '2024-02-21 13:40:00'),
(6151, 4044, 'Dinner at home with family', '2024-02-21 12:00:00'),
(6152, 4041, 'Checked in: Online webinar (early evening)', '2024-02-21 10:30:00'),
(6153, 4042, 'Posted photo from the same event, late', '2024-02-21 15:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8150, 3005, 4042, 4040, 9, 'Heated call at 09:10, argument about money'),
(8151, 3005, 4041, 4040, 4, 'Call at 13:20, short and tense, 40 minutes before incident'),
(8152, 3005, 4041, 4044, 6, 'Call at 15:05, right after incident; location pinged near the scene'),
(8153, 3005, 4044, 4041, 2, 'Call at 16:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9150, 4040, 4041, 'Accountant'),
(9151, 4040, 4042, 'Acquaintance with prior dispute'),
(9152, 4041, 4044, 'Cousins'),
(9153, 4040, 4043, 'Neighbor');

-- ---------- Case 3006: Robbery in Lakeview ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3006, '2024-03-05', 'Robbery', 'Robbery at Corner Pharmacy. Masked person took cash and opioids at closing time, left in a car. Incident window around 21:00-22:30. Witness saw a red Audi leaving the scene.', 'Lakeview');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5050, 59, 172.0, 'blue', 'gray', 'male', 'FJ-1185', 'Honda', 'Civic'),
(5051, 51, 190.0, 'green', 'black', 'female', 'EP-1296', 'Audi', 'Q5'),
(5052, 50, 177.0, 'hazel', 'brown', 'male', 'PT-1407', 'Audi', 'Sedan'),
(5053, 38, 164.0, 'brown', 'gray', 'male', 'UY-1518', 'Toyota', 'Corolla'),
(5054, 35, 164.0, 'blue', 'gray', 'female', 'ZD-1629', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4050, 'Brian Crane', 'Lakeview', 5050),
(4051, 'Cora Joyce', 'Lakeview', 5051),
(4052, 'Dennis Baker', 'Lakeview', 5052),
(4053, 'Edwin Irwin', 'Lakeview', 5053),
(4054, 'Fiona Price', 'Springfield', 5054);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3006, 4053, 'Witness', 'Saw a red Audi leave fast around 21:15. Partial plate began with ''EP''. Driver had black hair and looked female.'),
(3006, 4051, 'Person of Interest', 'Denies involvement. Says she was at Movie theater all evening and has had no contact with Brian Crane recently.'),
(3006, 4052, 'Person of Interest', 'Admits to a recent argument with Brian Crane but says he was at a public event all night. Drives a Audi.'),
(3006, 4054, 'Associate', 'Says Cora Joyce is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6160, 4052, 'Public event with friends - photo tagged', '2024-03-05 20:40:00'),
(6161, 4054, 'Dinner at home with family', '2024-03-05 19:00:00'),
(6162, 4051, 'Checked in: Movie theater (early evening)', '2024-03-05 17:30:00'),
(6163, 4052, 'Posted photo from the same event, late', '2024-03-05 22:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8160, 3006, 4052, 4050, 9, 'Heated call at 16:10, argument about money'),
(8161, 3006, 4051, 4050, 4, 'Call at 20:20, short and tense, 40 minutes before incident'),
(8162, 3006, 4051, 4054, 6, 'Call at 22:05, right after incident; location pinged near the scene'),
(8163, 3006, 4054, 4051, 2, 'Call at 23:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9160, 4050, 4051, 'Regular customer'),
(9161, 4050, 4052, 'Acquaintance with prior dispute'),
(9162, 4051, 4054, 'Cousins'),
(9163, 4050, 4053, 'Neighbor');

-- ---------- Case 3007: Kidnapping in Oakridge ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3007, '2024-03-19', 'Kidnapping', 'Kidnapping at Westside Elementary parking lot. Child taken briefly by a person claiming to be a relative; later found unharmed. Incident window around 18:00-19:30. Witness saw a silver Nissan leaving the scene.', 'Oakridge');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5060, 60, 175.0, 'blue', 'black', 'male', 'GJ-1222', 'Honda', 'Civic'),
(5061, 56, 158.0, 'hazel', 'brown', 'male', 'FS-1333', 'Nissan', 'Altima'),
(5062, 59, 180.0, 'hazel', 'gray', 'female', 'QV-1444', 'Nissan', 'Sedan'),
(5063, 49, 180.0, 'brown', 'red', 'male', 'VB-1555', 'Toyota', 'Corolla'),
(5064, 54, 180.0, 'brown', 'blonde', 'female', 'AH-1666', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4060, 'Gordon Walsh', 'Oakridge', 5060),
(4061, 'Harold Eaton', 'Oakridge', 5061),
(4062, 'Irene Lyons', 'Oakridge', 5062),
(4063, 'Jonas Dixon', 'Oakridge', 5063),
(4064, 'Karen Keller', 'Springfield', 5064);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3007, 4063, 'Witness', 'Saw a silver Nissan leave fast around 18:15. Partial plate began with ''FS''. Driver had brown hair and looked male.'),
(3007, 4061, 'Person of Interest', 'Denies involvement. Says he was at Church choir practice all evening and has had no contact with Gordon Walsh recently.'),
(3007, 4062, 'Person of Interest', 'Admits to a recent argument with Gordon Walsh but says she was at a public event all night. Drives a Nissan.'),
(3007, 4064, 'Associate', 'Says Harold Eaton is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6170, 4062, 'Public event with friends - photo tagged', '2024-03-19 17:40:00'),
(6171, 4064, 'Dinner at home with family', '2024-03-19 16:00:00'),
(6172, 4061, 'Checked in: Church choir practice (early evening)', '2024-03-19 14:30:00'),
(6173, 4062, 'Posted photo from the same event, late', '2024-03-19 19:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8170, 3007, 4062, 4060, 9, 'Heated call at 13:10, argument about money'),
(8171, 3007, 4061, 4060, 4, 'Call at 17:20, short and tense, 40 minutes before incident'),
(8172, 3007, 4061, 4064, 6, 'Call at 19:05, right after incident; location pinged near the scene'),
(8173, 3007, 4064, 4061, 2, 'Call at 20:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9170, 4060, 4061, 'Estranged relative'),
(9171, 4060, 4062, 'Acquaintance with prior dispute'),
(9172, 4061, 4064, 'Cousins'),
(9173, 4060, 4063, 'Neighbor');

-- ---------- Case 3008: Embezzlement in Springfield ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3008, '2024-04-02', 'Embezzlement', 'Embezzlement at Delta Logistics HQ. Seven transfers under audit threshold moved funds to a personal account. Incident window around 11:00-12:30. Witness saw a black Hyundai leaving the scene.', 'Springfield');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5070, 60, 164.0, 'brown', 'blonde', 'male', 'HJ-1259', 'Honda', 'Civic'),
(5071, 47, 156.0, 'brown', 'red', 'male', 'GV-1370', 'Hyundai', 'Elantra'),
(5072, 37, 179.0, 'blue', 'black', 'male', 'RX-1481', 'Hyundai', 'Sedan'),
(5073, 40, 177.0, 'green', 'black', 'female', 'WE-1592', 'Toyota', 'Corolla'),
(5074, 54, 162.0, 'brown', 'black', 'male', 'BL-1703', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4070, 'Liam Reed', 'Springfield', 5070),
(4071, 'Mason Zimmer', 'Springfield', 5071),
(4072, 'Nolan Gibbs', 'Springfield', 5072),
(4073, 'Opal Noble', 'Springfield', 5073),
(4074, 'Peter Foster', 'Oakridge', 5074);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3008, 4073, 'Witness', 'Saw a black Hyundai leave fast around 11:15. Partial plate began with ''GV''. Driver had red hair and looked male.'),
(3008, 4071, 'Person of Interest', 'Denies involvement. Says he was at Doctor appointment all evening and has had no contact with Liam Reed recently.'),
(3008, 4072, 'Person of Interest', 'Admits to a recent argument with Liam Reed but says he was at a public event all night. Drives a Hyundai.'),
(3008, 4074, 'Associate', 'Says Mason Zimmer is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6180, 4072, 'Public event with friends - photo tagged', '2024-04-02 10:40:00'),
(6181, 4074, 'Dinner at home with family', '2024-04-02 09:00:00'),
(6182, 4071, 'Checked in: Doctor appointment (early evening)', '2024-04-02 07:30:00'),
(6183, 4072, 'Posted photo from the same event, late', '2024-04-02 12:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8180, 3008, 4072, 4070, 9, 'Heated call at 06:10, argument about money'),
(8181, 3008, 4071, 4070, 4, 'Call at 10:20, short and tense, 40 minutes before incident'),
(8182, 3008, 4071, 4074, 6, 'Call at 12:05, right after incident; location pinged near the scene'),
(8183, 3008, 4074, 4071, 2, 'Call at 13:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9180, 4070, 4071, 'Payroll clerk'),
(9181, 4070, 4072, 'Acquaintance with prior dispute'),
(9182, 4071, 4074, 'Cousins'),
(9183, 4070, 4073, 'Neighbor');

-- ---------- Case 3009: Vandalism in Riverdale ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3009, '2024-04-16', 'Vandalism', 'Vandalism at City Art Museum. Sculpture garden spray-painted and security camera lens covered. Incident window around 01:00-02:30. Witness saw a black Kia leaving the scene.', 'Riverdale');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5080, 34, 188.0, 'brown', 'brown', 'female', 'IJ-1296', 'Honda', 'Civic'),
(5081, 37, 188.0, 'green', 'black', 'female', 'HY-1407', 'Kia', 'Sorento'),
(5082, 33, 189.0, 'brown', 'blonde', 'female', 'SZ-1518', 'Kia', 'Sedan'),
(5083, 57, 174.0, 'brown', 'blonde', 'female', 'XH-1629', 'Toyota', 'Corolla'),
(5084, 40, 188.0, 'green', 'gray', 'female', 'CP-1740', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4080, 'Quinn Mercer', 'Riverdale', 5080),
(4081, 'Rita Turner', 'Riverdale', 5081),
(4082, 'Sara Bishop', 'Riverdale', 5082),
(4083, 'Tessa Ingram', 'Riverdale', 5083),
(4084, 'Alice Adams', 'Springfield', 5084);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3009, 4083, 'Witness', 'Saw a black Kia leave fast around 01:15. Partial plate began with ''HY''. Driver had black hair and looked female.'),
(3009, 4081, 'Person of Interest', 'Denies involvement. Says she was at Open mic night all evening and has had no contact with Quinn Mercer recently.'),
(3009, 4082, 'Person of Interest', 'Admits to a recent argument with Quinn Mercer but says she was at a public event all night. Drives a Kia.'),
(3009, 4084, 'Associate', 'Says Rita Turner is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6190, 4082, 'Public event with friends - photo tagged', '2024-04-16 00:40:00'),
(6191, 4084, 'Dinner at home with family', '2024-04-16 23:00:00'),
(6192, 4081, 'Checked in: Open mic night (early evening)', '2024-04-16 21:30:00'),
(6193, 4082, 'Posted photo from the same event, late', '2024-04-16 02:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8190, 3009, 4082, 4080, 9, 'Heated call at -4:10, argument about money'),
(8191, 3009, 4081, 4080, 4, 'Call at 00:20, short and tense, 40 minutes before incident'),
(8192, 3009, 4081, 4084, 6, 'Call at 02:05, right after incident; location pinged near the scene'),
(8193, 3009, 4084, 4081, 2, 'Call at 03:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9190, 4080, 4081, 'Rejected artist'),
(9191, 4080, 4082, 'Acquaintance with prior dispute'),
(9192, 4081, 4084, 'Cousins'),
(9193, 4080, 4083, 'Neighbor');

-- ---------- Case 3010: Assault in Lakeview ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3010, '2024-05-03', 'Assault', 'Assault at The Rusty Anchor bar. Victim hit with a bottle in the parking lot after closing. Incident window around 23:00-00:30. Witness saw a blue Subaru leaving the scene.', 'Lakeview');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5090, 57, 186.0, 'green', 'brown', 'male', 'JJ-1333', 'Honda', 'Civic'),
(5091, 25, 156.0, 'green', 'brown', 'female', 'IB-1444', 'Subaru', 'Outback'),
(5092, 54, 171.0, 'blue', 'gray', 'male', 'TB-1555', 'Subaru', 'Sedan'),
(5093, 46, 183.0, 'green', 'brown', 'female', 'YK-1666', 'Toyota', 'Corolla'),
(5094, 47, 160.0, 'blue', 'brown', 'male', 'DT-1777', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4090, 'Brian Hayes', 'Lakeview', 5090),
(4091, 'Cora Owens', 'Lakeview', 5091),
(4092, 'Dennis Vance', 'Lakeview', 5092),
(4093, 'Elena Doyle', 'Lakeview', 5093),
(4094, 'Felix Knox', 'Springfield', 5094);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3010, 4093, 'Witness', 'Saw a blue Subaru leave fast around 23:15. Partial plate began with ''IB''. Driver had brown hair and looked female.'),
(3010, 4091, 'Person of Interest', 'Denies involvement. Says she was at Friend''s birthday dinner all evening and has had no contact with Brian Hayes recently.'),
(3010, 4092, 'Person of Interest', 'Admits to a recent argument with Brian Hayes but says he was at a public event all night. Drives a Subaru.'),
(3010, 4094, 'Associate', 'Says Cora Owens is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6200, 4092, 'Public event with friends - photo tagged', '2024-05-03 22:40:00'),
(6201, 4094, 'Dinner at home with family', '2024-05-03 21:00:00'),
(6202, 4091, 'Checked in: Friend''s birthday dinner (early evening)', '2024-05-03 19:30:00'),
(6203, 4092, 'Posted photo from the same event, late', '2024-05-03 00:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8200, 3010, 4092, 4090, 9, 'Heated call at 18:10, argument about money'),
(8201, 3010, 4091, 4090, 4, 'Call at 22:20, short and tense, 40 minutes before incident'),
(8202, 3010, 4091, 4094, 6, 'Call at 00:05, right after incident; location pinged near the scene'),
(8203, 3010, 4094, 4091, 2, 'Call at 01:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9200, 4090, 4091, 'Bar patron'),
(9201, 4090, 4092, 'Acquaintance with prior dispute'),
(9202, 4091, 4094, 'Cousins'),
(9203, 4090, 4093, 'Neighbor');

-- ---------- Case 3011: Hit and Run in Oakridge ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3011, '2024-05-18', 'Hit and Run', 'Hit and Run at Pine Avenue crosswalk. Victim struck by a car that fled; broken headlight fragments recovered. Incident window around 20:00-21:30. Witness saw a white Mazda leaving the scene.', 'Oakridge');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5100, 46, 160.0, 'brown', 'gray', 'male', 'KJ-1370', 'Honda', 'Civic'),
(5101, 48, 167.0, 'hazel', 'red', 'male', 'JE-1481', 'Mazda', 'CX-5'),
(5102, 35, 182.0, 'green', 'red', 'female', 'UD-1592', 'Mazda', 'Sedan'),
(5103, 29, 180.0, 'hazel', 'black', 'male', 'ZN-1703', 'Toyota', 'Corolla'),
(5104, 49, 160.0, 'blue', 'gray', 'female', 'EX-1814', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4100, 'Gordon Carter', 'Oakridge', 5100),
(4101, 'Harold Jensen', 'Oakridge', 5101),
(4102, 'Irene Quigley', 'Oakridge', 5102),
(4103, 'Jonas Yates', 'Oakridge', 5103),
(4104, 'Karen Frost', 'Springfield', 5104);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3011, 4103, 'Witness', 'Saw a white Mazda leave fast around 20:15. Partial plate began with ''JE''. Driver had red hair and looked male.'),
(3011, 4101, 'Person of Interest', 'Denies involvement. Says he was at Warehouse night shift all evening and has had no contact with Gordon Carter recently.'),
(3011, 4102, 'Person of Interest', 'Admits to a recent argument with Gordon Carter but says she was at a public event all night. Drives a Mazda.'),
(3011, 4104, 'Associate', 'Says Harold Jensen is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6210, 4102, 'Public event with friends - photo tagged', '2024-05-18 19:40:00'),
(6211, 4104, 'Dinner at home with family', '2024-05-18 18:00:00'),
(6212, 4101, 'Checked in: Warehouse night shift (early evening)', '2024-05-18 16:30:00'),
(6213, 4102, 'Posted photo from the same event, late', '2024-05-18 21:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8210, 3011, 4102, 4100, 9, 'Heated call at 15:10, argument about money'),
(8211, 3011, 4101, 4100, 4, 'Call at 19:20, short and tense, 40 minutes before incident'),
(8212, 3011, 4101, 4104, 6, 'Call at 21:05, right after incident; location pinged near the scene'),
(8213, 3011, 4104, 4101, 2, 'Call at 22:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9210, 4100, 4101, 'Delivery driver'),
(9211, 4100, 4102, 'Acquaintance with prior dispute'),
(9212, 4101, 4104, 'Cousins'),
(9213, 4100, 4103, 'Neighbor');

-- ---------- Case 3012: Theft in Springfield ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3012, '2024-06-01', 'Theft', 'Theft at Sunrise Antiques. Rare pocket watch taken from a locked case during store hours. Incident window around 15:00-16:30. Witness saw a green Chevrolet leaving the scene.', 'Springfield');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5110, 33, 190.0, 'blue', 'brown', 'male', 'LJ-1407', 'Honda', 'Civic'),
(5111, 25, 155.0, 'brown', 'red', 'male', 'KH-1518', 'Chevrolet', 'Malibu'),
(5112, 57, 163.0, 'hazel', 'red', 'male', 'VF-1629', 'Chevrolet', 'Sedan'),
(5113, 36, 168.0, 'brown', 'gray', 'male', 'AQ-1740', 'Toyota', 'Corolla'),
(5114, 40, 168.0, 'green', 'blonde', 'female', 'FB-1851', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4110, 'Liam Moss', 'Springfield', 5110),
(4111, 'Mason Ellis', 'Springfield', 5111),
(4112, 'Nolan Lowe', 'Springfield', 5112),
(4113, 'Oscar Stone', 'Springfield', 5113),
(4114, 'Priya Archer', 'Oakridge', 5114);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3012, 4113, 'Witness', 'Saw a green Chevrolet leave fast around 15:15. Partial plate began with ''KH''. Driver had red hair and looked male.'),
(3012, 4111, 'Person of Interest', 'Denies involvement. Says he was at Coffee shop meetup all evening and has had no contact with Liam Moss recently.'),
(3012, 4112, 'Person of Interest', 'Admits to a recent argument with Liam Moss but says he was at a public event all night. Drives a Chevrolet.'),
(3012, 4114, 'Associate', 'Says Mason Ellis is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6220, 4112, 'Public event with friends - photo tagged', '2024-06-01 14:40:00'),
(6221, 4114, 'Dinner at home with family', '2024-06-01 13:00:00'),
(6222, 4111, 'Checked in: Coffee shop meetup (early evening)', '2024-06-01 11:30:00'),
(6223, 4112, 'Posted photo from the same event, late', '2024-06-01 16:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8220, 3012, 4112, 4110, 9, 'Heated call at 10:10, argument about money'),
(8221, 3012, 4111, 4110, 4, 'Call at 14:20, short and tense, 40 minutes before incident'),
(8222, 3012, 4111, 4114, 6, 'Call at 16:05, right after incident; location pinged near the scene'),
(8223, 3012, 4114, 4111, 2, 'Call at 17:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9220, 4110, 4111, 'Appraiser'),
(9221, 4110, 4112, 'Acquaintance with prior dispute'),
(9222, 4111, 4114, 'Cousins'),
(9223, 4110, 4113, 'Neighbor');

-- ---------- Case 3013: Blackmail in Riverdale ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3013, '2024-06-14', 'Blackmail', 'Blackmail at Councilman''s residence. Anonymous letters demanded money, with photographs attached. Incident window around 19:00-20:30. Witness saw a black Volkswagen leaving the scene.', 'Riverdale');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5120, 56, 163.0, 'blue', 'blonde', 'male', 'MJ-1444', 'Honda', 'Civic'),
(5121, 57, 187.0, 'brown', 'gray', 'female', 'LK-1555', 'Volkswagen', 'Passat'),
(5122, 52, 166.0, 'brown', 'red', 'female', 'WH-1666', 'Volkswagen', 'Sedan'),
(5123, 33, 166.0, 'blue', 'red', 'female', 'BT-1777', 'Toyota', 'Corolla'),
(5124, 54, 162.0, 'brown', 'gray', 'male', 'GF-1888', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4120, 'Quentin Holt', 'Riverdale', 5120),
(4121, 'Rita Oakes', 'Riverdale', 5121),
(4122, 'Sara Grant', 'Riverdale', 5122),
(4123, 'Tessa Nash', 'Riverdale', 5123),
(4124, 'Aaron Upton', 'Springfield', 5124);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3013, 4123, 'Witness', 'Saw a black Volkswagen leave fast around 19:15. Partial plate began with ''LK''. Driver had gray hair and looked female.'),
(3013, 4121, 'Person of Interest', 'Denies involvement. Says she was at Campaign fundraiser all evening and has had no contact with Quentin Holt recently.'),
(3013, 4122, 'Person of Interest', 'Admits to a recent argument with Quentin Holt but says she was at a public event all night. Drives a Volkswagen.'),
(3013, 4124, 'Associate', 'Says Rita Oakes is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6230, 4122, 'Public event with friends - photo tagged', '2024-06-14 18:40:00'),
(6231, 4124, 'Dinner at home with family', '2024-06-14 17:00:00'),
(6232, 4121, 'Checked in: Campaign fundraiser (early evening)', '2024-06-14 15:30:00'),
(6233, 4122, 'Posted photo from the same event, late', '2024-06-14 20:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8230, 3013, 4122, 4120, 9, 'Heated call at 14:10, argument about money'),
(8231, 3013, 4121, 4120, 4, 'Call at 18:20, short and tense, 40 minutes before incident'),
(8232, 3013, 4121, 4124, 6, 'Call at 20:05, right after incident; location pinged near the scene'),
(8233, 3013, 4124, 4121, 2, 'Call at 21:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9230, 4120, 4121, 'Campaign aide'),
(9231, 4120, 4122, 'Acquaintance with prior dispute'),
(9232, 4121, 4124, 'Cousins'),
(9233, 4120, 4123, 'Neighbor');

-- ---------- Case 3014: Poisoning in Oakridge ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3014, '2024-07-04', 'Poisoning', 'Poisoning at Fairview Country Club. Victim collapsed after a toast; toxin found in wine glass. Incident window around 20:00-21:30. Witness saw a white Tesla leaving the scene.', 'Oakridge');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5130, 59, 156.0, 'brown', 'blonde', 'female', 'NJ-1481', 'Honda', 'Civic'),
(5131, 52, 175.0, 'blue', 'black', 'female', 'MN-1592', 'Tesla', 'Model Y'),
(5132, 41, 183.0, 'hazel', 'black', 'male', 'XJ-1703', 'Tesla', 'Sedan'),
(5133, 56, 170.0, 'green', 'red', 'male', 'CW-1814', 'Toyota', 'Corolla'),
(5134, 59, 167.0, 'hazel', 'gray', 'male', 'HJ-1925', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4130, 'Bella Crane', 'Oakridge', 5130),
(4131, 'Cora Joyce', 'Oakridge', 5131),
(4132, 'Dennis Baker', 'Oakridge', 5132),
(4133, 'Edwin Irwin', 'Oakridge', 5133),
(4134, 'Felix Price', 'Springfield', 5134);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3014, 4133, 'Witness', 'Saw a white Tesla leave fast around 20:15. Partial plate began with ''MN''. Driver had black hair and looked female.'),
(3014, 4131, 'Person of Interest', 'Denies involvement. Says she was at Kitchen supplier visit all evening and has had no contact with Bella Crane recently.'),
(3014, 4132, 'Person of Interest', 'Admits to a recent argument with Bella Crane but says he was at a public event all night. Drives a Tesla.'),
(3014, 4134, 'Associate', 'Says Cora Joyce is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6240, 4132, 'Public event with friends - photo tagged', '2024-07-04 19:40:00'),
(6241, 4134, 'Dinner at home with family', '2024-07-04 18:00:00'),
(6242, 4131, 'Checked in: Kitchen supplier visit (early evening)', '2024-07-04 16:30:00'),
(6243, 4132, 'Posted photo from the same event, late', '2024-07-04 21:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8240, 3014, 4132, 4130, 9, 'Heated call at 15:10, argument about money'),
(8241, 3014, 4131, 4130, 4, 'Call at 19:20, short and tense, 40 minutes before incident'),
(8242, 3014, 4131, 4134, 6, 'Call at 21:05, right after incident; location pinged near the scene'),
(8243, 3014, 4134, 4131, 2, 'Call at 22:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9240, 4130, 4131, 'Club caterer'),
(9241, 4130, 4132, 'Acquaintance with prior dispute'),
(9242, 4131, 4134, 'Cousins'),
(9243, 4130, 4133, 'Neighbor');

-- ---------- Case 3015: Cyber Intrusion in Lakeview ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3015, '2024-07-22', 'Cyber Intrusion', 'Cyber Intrusion at Nexus Software. Source code stolen via a VPN account that was not used in the office. Incident window around 03:00-04:30. Witness saw a silver Jeep leaving the scene.', 'Lakeview');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5140, 43, 162.0, 'blue', 'black', 'male', 'OJ-1518', 'Honda', 'Civic'),
(5141, 47, 164.0, 'green', 'brown', 'female', 'NQ-1629', 'Jeep', 'Wrangler'),
(5142, 32, 184.0, 'blue', 'gray', 'male', 'YL-1740', 'Jeep', 'Sedan'),
(5143, 30, 180.0, 'hazel', 'black', 'female', 'DZ-1851', 'Toyota', 'Corolla'),
(5144, 34, 169.0, 'blue', 'brown', 'female', 'IN-1962', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4140, 'Gordon Walsh', 'Lakeview', 5140),
(4141, 'Hana Eaton', 'Lakeview', 5141),
(4142, 'Ivan Lyons', 'Lakeview', 5142),
(4143, 'Julia Dixon', 'Lakeview', 5143),
(4144, 'Karen Keller', 'Springfield', 5144);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3015, 4143, 'Witness', 'Saw a silver Jeep leave fast around 03:15. Partial plate began with ''NQ''. Driver had brown hair and looked female.'),
(3015, 4141, 'Person of Interest', 'Denies involvement. Says she was at Sleepover at friend''s all evening and has had no contact with Gordon Walsh recently.'),
(3015, 4142, 'Person of Interest', 'Admits to a recent argument with Gordon Walsh but says he was at a public event all night. Drives a Jeep.'),
(3015, 4144, 'Associate', 'Says Hana Eaton is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6250, 4142, 'Public event with friends - photo tagged', '2024-07-22 02:40:00'),
(6251, 4144, 'Dinner at home with family', '2024-07-22 01:00:00'),
(6252, 4141, 'Checked in: Sleepover at friend''s (early evening)', '2024-07-22 23:30:00'),
(6253, 4142, 'Posted photo from the same event, late', '2024-07-22 04:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8250, 3015, 4142, 4140, 9, 'Heated call at -2:10, argument about money'),
(8251, 3015, 4141, 4140, 4, 'Call at 02:20, short and tense, 40 minutes before incident'),
(8252, 3015, 4141, 4144, 6, 'Call at 04:05, right after incident; location pinged near the scene'),
(8253, 3015, 4144, 4141, 2, 'Call at 05:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9250, 4140, 4141, 'Contract developer'),
(9251, 4140, 4142, 'Acquaintance with prior dispute'),
(9252, 4141, 4144, 'Cousins'),
(9253, 4140, 4143, 'Neighbor');

-- ---------- Case 3016: Forgery in Springfield ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3016, '2024-08-09', 'Forgery', 'Forgery at Greystone Notary. Property deed forged with a copied stamp and a signature. Incident window around 16:00-17:30. Witness saw a silver Volvo leaving the scene.', 'Springfield');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5150, 59, 184.0, 'hazel', 'blonde', 'female', 'PJ-1555', 'Honda', 'Civic'),
(5151, 25, 179.0, 'green', 'black', 'female', 'OT-1666', 'Volvo', 'XC60'),
(5152, 57, 173.0, 'brown', 'blonde', 'female', 'ZN-1777', 'Volvo', 'Sedan'),
(5153, 31, 169.0, 'brown', 'black', 'female', 'EC-1888', 'Toyota', 'Corolla'),
(5154, 29, 171.0, 'green', 'blonde', 'male', 'JR-1999', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4150, 'Laura Reed', 'Springfield', 5150),
(4151, 'Maya Zimmer', 'Springfield', 5151),
(4152, 'Nina Gibbs', 'Springfield', 5152),
(4153, 'Opal Noble', 'Springfield', 5153),
(4154, 'Peter Foster', 'Oakridge', 5154);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3016, 4153, 'Witness', 'Saw a silver Volvo leave fast around 16:15. Partial plate began with ''OT''. Driver had black hair and looked female.'),
(3016, 4151, 'Person of Interest', 'Denies involvement. Says she was at Open house showing all evening and has had no contact with Laura Reed recently.'),
(3016, 4152, 'Person of Interest', 'Admits to a recent argument with Laura Reed but says she was at a public event all night. Drives a Volvo.'),
(3016, 4154, 'Associate', 'Says Maya Zimmer is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6260, 4152, 'Public event with friends - photo tagged', '2024-08-09 15:40:00'),
(6261, 4154, 'Dinner at home with family', '2024-08-09 14:00:00'),
(6262, 4151, 'Checked in: Open house showing (early evening)', '2024-08-09 12:30:00'),
(6263, 4152, 'Posted photo from the same event, late', '2024-08-09 17:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8260, 3016, 4152, 4150, 9, 'Heated call at 11:10, argument about money'),
(8261, 3016, 4151, 4150, 4, 'Call at 15:20, short and tense, 40 minutes before incident'),
(8262, 3016, 4151, 4154, 6, 'Call at 17:05, right after incident; location pinged near the scene'),
(8263, 3016, 4154, 4151, 2, 'Call at 18:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9260, 4150, 4151, 'Real-estate agent'),
(9261, 4150, 4152, 'Acquaintance with prior dispute'),
(9262, 4151, 4154, 'Cousins'),
(9263, 4150, 4153, 'Neighbor');

-- ---------- Case 3017: Extortion in Riverdale ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3017, '2024-08-25', 'Extortion', 'Extortion at Luigi''s Pizzeria. Weekly cash demands with threats of damage to property. Incident window around 17:00-18:30. Witness saw a green Lexus leaving the scene.', 'Riverdale');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5160, 60, 186.0, 'green', 'blonde', 'male', 'QJ-1592', 'Honda', 'Civic'),
(5161, 29, 172.0, 'brown', 'gray', 'male', 'PW-1703', 'Lexus', 'ES'),
(5162, 35, 182.0, 'brown', 'brown', 'female', 'AP-1814', 'Lexus', 'Sedan'),
(5163, 41, 156.0, 'brown', 'red', 'male', 'FF-1925', 'Toyota', 'Corolla'),
(5164, 40, 160.0, 'blue', 'red', 'female', 'KV-2036', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4160, 'Quentin Mercer', 'Riverdale', 5160),
(4161, 'Rohan Turner', 'Riverdale', 5161),
(4162, 'Sara Bishop', 'Riverdale', 5162),
(4163, 'Tobias Ingram', 'Riverdale', 5163),
(4164, 'Alice Adams', 'Springfield', 5164);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3017, 4163, 'Witness', 'Saw a green Lexus leave fast around 17:15. Partial plate began with ''PW''. Driver had gray hair and looked male.'),
(3017, 4161, 'Person of Interest', 'Denies involvement. Says he was at Trade fair all evening and has had no contact with Quentin Mercer recently.'),
(3017, 4162, 'Person of Interest', 'Admits to a recent argument with Quentin Mercer but says she was at a public event all night. Drives a Lexus.'),
(3017, 4164, 'Associate', 'Says Rohan Turner is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6270, 4162, 'Public event with friends - photo tagged', '2024-08-25 16:40:00'),
(6271, 4164, 'Dinner at home with family', '2024-08-25 15:00:00'),
(6272, 4161, 'Checked in: Trade fair (early evening)', '2024-08-25 13:30:00'),
(6273, 4162, 'Posted photo from the same event, late', '2024-08-25 18:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8270, 3017, 4162, 4160, 9, 'Heated call at 12:10, argument about money'),
(8271, 3017, 4161, 4160, 4, 'Call at 16:20, short and tense, 40 minutes before incident'),
(8272, 3017, 4161, 4164, 6, 'Call at 18:05, right after incident; location pinged near the scene'),
(8273, 3017, 4164, 4161, 2, 'Call at 19:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9270, 4160, 4161, 'Supplier'),
(9271, 4160, 4162, 'Acquaintance with prior dispute'),
(9272, 4161, 4164, 'Cousins'),
(9273, 4160, 4163, 'Neighbor');

-- ---------- Case 3018: Smuggling in Lakeview ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3018, '2024-09-11', 'Smuggling', 'Smuggling at Harbor Docks. Containers carrying unlisted goods cleared after altered paperwork. Incident window around 04:00-05:30. Witness saw a silver Dodge leaving the scene.', 'Lakeview');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5170, 26, 188.0, 'blue', 'red', 'male', 'RJ-1629', 'Honda', 'Civic'),
(5171, 31, 165.0, 'green', 'gray', 'female', 'QZ-1740', 'Dodge', 'Charger'),
(5172, 27, 166.0, 'blue', 'blonde', 'male', 'BR-1851', 'Dodge', 'Sedan'),
(5173, 43, 174.0, 'blue', 'red', 'female', 'GI-1962', 'Toyota', 'Corolla'),
(5174, 42, 183.0, 'blue', 'brown', 'male', 'LZ-2073', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4170, 'Brian Hayes', 'Lakeview', 5170),
(4171, 'Cora Owens', 'Lakeview', 5171),
(4172, 'Dennis Vance', 'Lakeview', 5172),
(4173, 'Elena Doyle', 'Lakeview', 5173),
(4174, 'Felix Knox', 'Springfield', 5174);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3018, 4173, 'Witness', 'Saw a silver Dodge leave fast around 04:15. Partial plate began with ''QZ''. Driver had gray hair and looked female.'),
(3018, 4171, 'Person of Interest', 'Denies involvement. Says she was at Conference call all evening and has had no contact with Brian Hayes recently.'),
(3018, 4172, 'Person of Interest', 'Admits to a recent argument with Brian Hayes but says he was at a public event all night. Drives a Dodge.'),
(3018, 4174, 'Associate', 'Says Cora Owens is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6280, 4172, 'Public event with friends - photo tagged', '2024-09-11 03:40:00'),
(6281, 4174, 'Dinner at home with family', '2024-09-11 02:00:00'),
(6282, 4171, 'Checked in: Conference call (early evening)', '2024-09-11 00:30:00'),
(6283, 4172, 'Posted photo from the same event, late', '2024-09-11 05:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8280, 3018, 4172, 4170, 9, 'Heated call at -1:10, argument about money'),
(8281, 3018, 4171, 4170, 4, 'Call at 03:20, short and tense, 40 minutes before incident'),
(8282, 3018, 4171, 4174, 6, 'Call at 05:05, right after incident; location pinged near the scene'),
(8283, 3018, 4174, 4171, 2, 'Call at 06:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9280, 4170, 4171, 'Freight broker'),
(9281, 4170, 4172, 'Acquaintance with prior dispute'),
(9282, 4171, 4174, 'Cousins'),
(9283, 4170, 4173, 'Neighbor');

-- ---------- Case 3019: Stalking in Oakridge ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3019, '2024-09-27', 'Stalking', 'Stalking at Willow Court. Victim followed home for three weeks; tracking device found on car. Incident window around 22:00-23:30. Witness saw a black Mercedes leaving the scene.', 'Oakridge');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5180, 54, 170.0, 'hazel', 'black', 'female', 'SJ-1666', 'Honda', 'Civic'),
(5181, 30, 182.0, 'hazel', 'red', 'female', 'RC-1777', 'Mercedes', 'C-Class'),
(5182, 58, 180.0, 'green', 'red', 'male', 'CT-1888', 'Mercedes', 'Sedan'),
(5183, 37, 169.0, 'green', 'brown', 'female', 'HL-1999', 'Toyota', 'Corolla'),
(5184, 36, 163.0, 'hazel', 'red', 'male', 'MD-2110', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4180, 'Grace Carter', 'Oakridge', 5180),
(4181, 'Hana Jensen', 'Oakridge', 5181),
(4182, 'Ivan Quigley', 'Oakridge', 5182),
(4183, 'Julia Yates', 'Oakridge', 5183),
(4184, 'Kevin Frost', 'Springfield', 5184);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3019, 4183, 'Witness', 'Saw a black Mercedes leave fast around 22:15. Partial plate began with ''RC''. Driver had red hair and looked female.'),
(3019, 4181, 'Person of Interest', 'Denies involvement. Says she was at Library study group all evening and has had no contact with Grace Carter recently.'),
(3019, 4182, 'Person of Interest', 'Admits to a recent argument with Grace Carter but says he was at a public event all night. Drives a Mercedes.'),
(3019, 4184, 'Associate', 'Says Hana Jensen is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6290, 4182, 'Public event with friends - photo tagged', '2024-09-27 21:40:00'),
(6291, 4184, 'Dinner at home with family', '2024-09-27 20:00:00'),
(6292, 4181, 'Checked in: Library study group (early evening)', '2024-09-27 18:30:00'),
(6293, 4182, 'Posted photo from the same event, late', '2024-09-27 23:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8290, 3019, 4182, 4180, 9, 'Heated call at 17:10, argument about money'),
(8291, 3019, 4181, 4180, 4, 'Call at 21:20, short and tense, 40 minutes before incident'),
(8292, 3019, 4181, 4184, 6, 'Call at 23:05, right after incident; location pinged near the scene'),
(8293, 3019, 4184, 4181, 2, 'Call at 00:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9290, 4180, 4181, 'Former student'),
(9291, 4180, 4182, 'Acquaintance with prior dispute'),
(9292, 4181, 4184, 'Cousins'),
(9293, 4180, 4183, 'Neighbor');

-- ---------- Case 3020: Homicide in Springfield ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3020, '2024-10-12', 'Homicide', 'Homicide at Lakeside cabin. Body found near cabin with signs of a struggle; guest list missing. Incident window around 21:00-22:30. Witness saw a gray Honda leaving the scene.', 'Springfield');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5190, 48, 187.0, 'green', 'blonde', 'female', 'TJ-1703', 'Honda', 'Civic'),
(5191, 39, 173.0, 'brown', 'gray', 'male', 'SF-1814', 'Honda', 'CR-V'),
(5192, 53, 166.0, 'blue', 'brown', 'male', 'DV-1925', 'Honda', 'Sedan'),
(5193, 41, 183.0, 'brown', 'black', 'male', 'IO-2036', 'Toyota', 'Corolla'),
(5194, 40, 178.0, 'green', 'black', 'male', 'NH-2147', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4190, 'Laura Moss', 'Springfield', 5190),
(4191, 'Mason Ellis', 'Springfield', 5191),
(4192, 'Nolan Lowe', 'Springfield', 5192),
(4193, 'Oscar Stone', 'Springfield', 5193),
(4194, 'Peter Archer', 'Oakridge', 5194);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3020, 4193, 'Witness', 'Saw a gray Honda leave fast around 21:15. Partial plate began with ''SF''. Driver had gray hair and looked male.'),
(3020, 4191, 'Person of Interest', 'Denies involvement. Says he was at Ranger station meeting all evening and has had no contact with Laura Moss recently.'),
(3020, 4192, 'Person of Interest', 'Admits to a recent argument with Laura Moss but says he was at a public event all night. Drives a Honda.'),
(3020, 4194, 'Associate', 'Says Mason Ellis is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6300, 4192, 'Public event with friends - photo tagged', '2024-10-12 20:40:00'),
(6301, 4194, 'Dinner at home with family', '2024-10-12 19:00:00'),
(6302, 4191, 'Checked in: Ranger station meeting (early evening)', '2024-10-12 17:30:00'),
(6303, 4192, 'Posted photo from the same event, late', '2024-10-12 22:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8300, 3020, 4192, 4190, 9, 'Heated call at 16:10, argument about money'),
(8301, 3020, 4191, 4190, 4, 'Call at 20:20, short and tense, 40 minutes before incident'),
(8302, 3020, 4191, 4194, 6, 'Call at 22:05, right after incident; location pinged near the scene'),
(8303, 3020, 4194, 4191, 2, 'Call at 23:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9300, 4190, 4191, 'Fellow guide'),
(9301, 4190, 4192, 'Acquaintance with prior dispute'),
(9302, 4191, 4194, 'Cousins'),
(9303, 4190, 4193, 'Neighbor');

-- ---------- Case 3021: Robbery in Riverdale ----------
INSERT INTO crime_scene_report (case_id, date, type, description, city) VALUES
(3021, '2024-11-03', 'Robbery', 'Robbery at First Union Bank. Armed robbery; getaway car abandoned two blocks away, with stolen plates. Incident window around 12:00-13:30. Witness saw a silver Ford leaving the scene.', 'Riverdale');
INSERT INTO drivers_license (id, age, height, eye_color, hair_color, gender, plate_number, car_make, car_model) VALUES
(5200, 54, 172.0, 'blue', 'brown', 'female', 'UJ-1740', 'Honda', 'Civic'),
(5201, 39, 187.0, 'brown', 'black', 'male', 'TI-1851', 'Ford', 'Mustang'),
(5202, 29, 171.0, 'brown', 'blonde', 'male', 'EX-1962', 'Ford', 'Sedan'),
(5203, 33, 180.0, 'brown', 'gray', 'female', 'JR-2073', 'Toyota', 'Corolla'),
(5204, 49, 156.0, 'green', 'black', 'male', 'OL-2184', 'Ford', 'Fusion');
INSERT INTO person (person_id, name, city, license_id) VALUES
(4200, 'Quinn Holt', 'Riverdale', 5200),
(4201, 'Rohan Oakes', 'Riverdale', 5201),
(4202, 'Samuel Grant', 'Riverdale', 5202),
(4203, 'Tessa Nash', 'Riverdale', 5203),
(4204, 'Aaron Upton', 'Springfield', 5204);
INSERT INTO interview (case_id, person_id, role, description) VALUES
(3021, 4203, 'Witness', 'Saw a silver Ford leave fast around 12:15. Partial plate began with ''TI''. Driver had black hair and looked male.'),
(3021, 4201, 'Person of Interest', 'Denies involvement. Says he was at Dentist appointment all evening and has had no contact with Quinn Holt recently.'),
(3021, 4202, 'Person of Interest', 'Admits to a recent argument with Quinn Holt but says he was at a public event all night. Drives a Ford.'),
(3021, 4204, 'Associate', 'Says Rohan Oakes is a close contact and was ''probably home'' that night. Seemed nervous during questioning.');
INSERT INTO facebook_event_checkin (event_id, person_id, event_description, date) VALUES
(6310, 4202, 'Public event with friends - photo tagged', '2024-11-03 11:40:00'),
(6311, 4204, 'Dinner at home with family', '2024-11-03 10:00:00'),
(6312, 4201, 'Checked in: Dentist appointment (early evening)', '2024-11-03 08:30:00'),
(6313, 4202, 'Posted photo from the same event, late', '2024-11-03 13:20:00');
INSERT INTO phone_records (record_id, case_id, caller_id, receiver_id, call_duration_mins, description) VALUES
(8310, 3021, 4202, 4200, 9, 'Heated call at 07:10, argument about money'),
(8311, 3021, 4201, 4200, 4, 'Call at 11:20, short and tense, 40 minutes before incident'),
(8312, 3021, 4201, 4204, 6, 'Call at 13:05, right after incident; location pinged near the scene'),
(8313, 3021, 4204, 4201, 2, 'Call at 14:00, asking if everything is fine');
INSERT INTO relationships (relationship_id, person1_id, person2_id, relation) VALUES
(9310, 4200, 4201, 'Security guard'),
(9311, 4200, 4202, 'Acquaintance with prior dispute'),
(9312, 4201, 4204, 'Cousins'),
(9313, 4200, 4203, 'Neighbor');

-- ---------- Solutions ----------
INSERT INTO solution (case_id, culprit_person_id, motive, key_evidence) VALUES
(3002, 4011, 'Revenge over dismissal', 'License AD-xxxx (Toyota Camry) matches the witness''s partial plate; phone call to Bella Hayes before and to Felix Knox right after; alibi at Bowling alley is only an early check-in.'),
(3003, 4021, 'Avoid eviction', 'License BG-xxxx (Honda Accord) matches the witness''s partial plate; phone call to Gordon Carter before and to Kevin Frost right after; alibi at Late shift at gym is only an early check-in.'),
(3004, 4031, 'Insurance payout', 'License CJ-xxxx (Ford Focus) matches the witness''s partial plate; phone call to Laura Moss before and to Priya Archer right after; alibi at Cousin''s house party is only an early check-in.'),
(3005, 4041, 'Personal debt', 'License DM-xxxx (BMW X3) matches the witness''s partial plate; phone call to Quentin Holt before and to Alice Upton right after; alibi at Online webinar is only an early check-in.'),
(3006, 4051, 'Drug dependence', 'License EP-xxxx (Audi Q5) matches the witness''s partial plate; phone call to Brian Crane before and to Fiona Price right after; alibi at Movie theater is only an early check-in.'),
(3007, 4061, 'Custody dispute', 'License FS-xxxx (Nissan Altima) matches the witness''s partial plate; phone call to Gordon Walsh before and to Karen Keller right after; alibi at Church choir practice is only an early check-in.'),
(3008, 4071, 'Gambling debts', 'License GV-xxxx (Hyundai Elantra) matches the witness''s partial plate; phone call to Liam Reed before and to Peter Foster right after; alibi at Doctor appointment is only an early check-in.'),
(3009, 4081, 'Resentment after exhibit rejection', 'License HY-xxxx (Kia Sorento) matches the witness''s partial plate; phone call to Quinn Mercer before and to Alice Adams right after; alibi at Open mic night is only an early check-in.'),
(3010, 4091, 'Bar tab dispute', 'License IB-xxxx (Subaru Outback) matches the witness''s partial plate; phone call to Brian Hayes before and to Felix Knox right after; alibi at Friend''s birthday dinner is only an early check-in.'),
(3011, 4101, 'Fear of losing job after drinking', 'License JE-xxxx (Mazda CX-5) matches the witness''s partial plate; phone call to Gordon Carter before and to Karen Frost right after; alibi at Warehouse night shift is only an early check-in.'),
(3012, 4111, 'Resale profit', 'License KH-xxxx (Chevrolet Malibu) matches the witness''s partial plate; phone call to Liam Moss before and to Priya Archer right after; alibi at Coffee shop meetup is only an early check-in.'),
(3013, 4121, 'Money to pay medical bills', 'License LK-xxxx (Volkswagen Passat) matches the witness''s partial plate; phone call to Quentin Holt before and to Aaron Upton right after; alibi at Campaign fundraiser is only an early check-in.'),
(3014, 4131, 'Inheritance from a will change', 'License MN-xxxx (Tesla Model Y) matches the witness''s partial plate; phone call to Bella Crane before and to Felix Price right after; alibi at Kitchen supplier visit is only an early check-in.'),
(3015, 4141, 'Sold code to competitor', 'License NQ-xxxx (Jeep Wrangler) matches the witness''s partial plate; phone call to Gordon Walsh before and to Karen Keller right after; alibi at Sleepover at friend''s is only an early check-in.'),
(3016, 4151, 'Commission on a fake sale', 'License OT-xxxx (Volvo XC60) matches the witness''s partial plate; phone call to Laura Reed before and to Peter Foster right after; alibi at Open house showing is only an early check-in.'),
(3017, 4161, 'Cover a failing business', 'License PW-xxxx (Lexus ES) matches the witness''s partial plate; phone call to Quentin Mercer before and to Alice Adams right after; alibi at Trade fair is only an early check-in.'),
(3018, 4171, 'Profit from contraband', 'License QZ-xxxx (Dodge Charger) matches the witness''s partial plate; phone call to Brian Hayes before and to Felix Knox right after; alibi at Conference call is only an early check-in.'),
(3019, 4181, 'Obsession', 'License RC-xxxx (Mercedes C-Class) matches the witness''s partial plate; phone call to Grace Carter before and to Kevin Frost right after; alibi at Library study group is only an early check-in.'),
(3020, 4191, 'Dispute over business takeover', 'License SF-xxxx (Honda CR-V) matches the witness''s partial plate; phone call to Laura Moss before and to Peter Archer right after; alibi at Ranger station meeting is only an early check-in.'),
(3021, 4201, 'Debt', 'License TI-xxxx (Ford Mustang) matches the witness''s partial plate; phone call to Quinn Holt before and to Aaron Upton right after; alibi at Dentist appointment is only an early check-in.');