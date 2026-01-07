DROP DATABASE IF EXISTS vpms_db;
CREATE DATABASE vpms_db;
USE vpms_db;

-- Table for Users (Auth)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'SECURITY') NOT NULL
);

-- Table for Parking Slots
CREATE TABLE IF NOT EXISTS slots (
    id INT AUTO_INCREMENT PRIMARY KEY,
    slot_number VARCHAR(20) UNIQUE NOT NULL,
    type ENUM('RESIDENT', 'VISITOR', 'SERVICE') NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'DISABLED') DEFAULT 'AVAILABLE'
);

-- Table for Resident Management
CREATE TABLE IF NOT EXISTS residents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    flat_number VARCHAR(10) NOT NULL,
    floor_number VARCHAR(5) NOT NULL,
    contact_number VARCHAR(15) NOT NULL,
    vehicle_number VARCHAR(20) UNIQUE NOT NULL,
    vehicle_name VARCHAR(50),
    vehicle_type ENUM('2W', '4W', 'EV') NOT NULL,
    slot_id INT,
    FOREIGN KEY (slot_id) REFERENCES slots(id)
);

-- Table for Parking Entries
CREATE TABLE IF NOT EXISTS parking_entries (
    entry_id INT AUTO_INCREMENT PRIMARY KEY,
    visitor_name VARCHAR(100),
    vehicle_name VARCHAR(50),
    vehicle_number VARCHAR(20) NOT NULL,
    vehicle_type ENUM('2W', '4W', 'EV', 'SERVICE') NOT NULL,
    contact_number VARCHAR(15),
    visit_type ENUM('RESIDENT', 'VISITOR', 'SERVICE') NOT NULL,
    reason VARCHAR(255),
    resident_name VARCHAR(100),
    flat_number VARCHAR(10),
    floor_number VARCHAR(5),
    slot_id INT,
    arrival_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expected_departure TIMESTAMP NULL,
    actual_departure TIMESTAMP NULL,
    parking_duration VARCHAR(50),
    status ENUM('PARKED', 'EXITED') DEFAULT 'PARKED',
    FOREIGN KEY (slot_id) REFERENCES slots(id),
    CONSTRAINT chk_visit_type CHECK (visit_type IN ('RESIDENT', 'VISITOR', 'SERVICE')),
    CONSTRAINT chk_status CHECK (status IN ('PARKED', 'EXITED'))
);

-- Insert Default Admin & Security
INSERT INTO users (username, password, role) VALUES ('VPMS', 'vpmsvpms', 'ADMIN');
INSERT INTO users (username, password, role) VALUES ('security', 'sec123', 'SECURITY');

-- Insert 120 Slots
-- 70 Resident Slots
INSERT INTO slots (slot_number, type) VALUES ('RES-01','RESIDENT'),('RES-02','RESIDENT'),('RES-03','RESIDENT'),('RES-04','RESIDENT'),('RES-05','RESIDENT'),('RES-06','RESIDENT'),('RES-07','RESIDENT'),('RES-08','RESIDENT'),('RES-09','RESIDENT'),('RES-10','RESIDENT'),
('RES-11','RESIDENT'),('RES-12','RESIDENT'),('RES-13','RESIDENT'),('RES-14','RESIDENT'),('RES-15','RESIDENT'),('RES-16','RESIDENT'),('RES-17','RESIDENT'),('RES-18','RESIDENT'),('RES-19','RESIDENT'),('RES-20','RESIDENT'),
('RES-21','RESIDENT'),('RES-22','RESIDENT'),('RES-23','RESIDENT'),('RES-24','RESIDENT'),('RES-25','RESIDENT'),('RES-26','RESIDENT'),('RES-27','RESIDENT'),('RES-28','RESIDENT'),('RES-29','RESIDENT'),('RES-30','RESIDENT'),
('RES-31','RESIDENT'),('RES-32','RESIDENT'),('RES-33','RESIDENT'),('RES-34','RESIDENT'),('RES-35','RESIDENT'),('RES-36','RESIDENT'),('RES-37','RESIDENT'),('RES-38','RESIDENT'),('RES-39','RESIDENT'),('RES-40','RESIDENT'),
('RES-41','RESIDENT'),('RES-42','RESIDENT'),('RES-43','RESIDENT'),('RES-44','RESIDENT'),('RES-45','RESIDENT'),('RES-46','RESIDENT'),('RES-47','RESIDENT'),('RES-48','RESIDENT'),('RES-49','RESIDENT'),('RES-50','RESIDENT'),
('RES-51','RESIDENT'),('RES-52','RESIDENT'),('RES-53','RESIDENT'),('RES-54','RESIDENT'),('RES-55','RESIDENT'),('RES-56','RESIDENT'),('RES-57','RESIDENT'),('RES-58','RESIDENT'),('RES-59','RESIDENT'),('RES-60','RESIDENT'),
('RES-61','RESIDENT'),('RES-62','RESIDENT'),('RES-63','RESIDENT'),('RES-64','RESIDENT'),('RES-65','RESIDENT'),('RES-66','RESIDENT'),('RES-67','RESIDENT'),('RES-68','RESIDENT'),('RES-69','RESIDENT'),('RES-70','RESIDENT');

-- 40 Visitor Slots
INSERT INTO slots (slot_number, type) VALUES ('VIS-01','VISITOR'),('VIS-02','VISITOR'),('VIS-03','VISITOR'),('VIS-04','VISITOR'),('VIS-05','VISITOR'),('VIS-06','VISITOR'),('VIS-07','VISITOR'),('VIS-08','VISITOR'),('VIS-09','VISITOR'),('VIS-10','VISITOR'),
('VIS-11','VISITOR'),('VIS-12','VISITOR'),('VIS-13','VISITOR'),('VIS-14','VISITOR'),('VIS-15','VISITOR'),('VIS-16','VISITOR'),('VIS-17','VISITOR'),('VIS-18','VISITOR'),('VIS-19','VISITOR'),('VIS-20','VISITOR'),
('VIS-21','VISITOR'),('VIS-22','VISITOR'),('VIS-23','VISITOR'),('VIS-24','VISITOR'),('VIS-25','VISITOR'),('VIS-26','VISITOR'),('VIS-27','VISITOR'),('VIS-28','VISITOR'),('VIS-29','VISITOR'),('VIS-30','VISITOR'),
('VIS-31','VISITOR'),('VIS-32','VISITOR'),('VIS-33','VISITOR'),('VIS-34','VISITOR'),('VIS-35','VISITOR'),('VIS-36','VISITOR'),('VIS-37','VISITOR'),('VIS-38','VISITOR'),('VIS-39','VISITOR'),('VIS-40','VISITOR');

-- 10 Service Slots
INSERT INTO slots (slot_number, type) VALUES ('SRV-01','SERVICE'),('SRV-02','SERVICE'),('SRV-03','SERVICE'),('SRV-04','SERVICE'),('SRV-05','SERVICE'),('SRV-06','SERVICE'),('SRV-07','SERVICE'),('SRV-08','SERVICE'),('SRV-09','SERVICE'),('SRV-10','SERVICE');

-- Insert 70 Fake Residents with Modern Marathi/Indian Names (Post-2000 Style)
INSERT INTO residents (name, flat_number, floor_number, contact_number, vehicle_number, vehicle_name, vehicle_type, slot_id) VALUES 
('Aarav Patil', '101', '1', '9011012345', 'MH-12-AP-2001', 'Virtus', '4W', 1),
('Vihaan Deshmukh', '102', '1', '9011012346', 'MH-12-VD-2002', 'Slavia', '4W', 2),
('Reyansh Kulkarni', '103', '1', '9011012347', 'MH-12-RK-2003', 'Hunter 350', '2W', 3),
('Saanvi Joshi', '104', '1', '9011012348', 'MH-12-SJ-2004', 'Ola S1 Pro', 'EV', 4),
('Advik Pawar', '201', '2', '9011012349', 'MH-12-AP-2005', 'XUV700', '4W', 5),
('Ishaan More', '202', '2', '9011012350', 'MH-12-IM-2006', 'Harrier', '4W', 6),
('Ananya Shinde', '203', '2', '9011012351', 'MH-12-AS-2007', 'Nexon EV', 'EV', 7),
('Vivaan Chavan', '204', '2', '9011012352', 'MH-12-VC-2008', 'MT-15', '2W', 8),
('Myra Gaikwad', '301', '3', '9011012353', 'MH-12-MG-2009', 'Kia Seltos', '4W', 9),
('Kabir Jadhav', '302', '3', '9011012354', 'MH-12-KJ-2010', 'Fortuner Legender', '4W', 10),
('Kiara Bhosale', '303', '3', '9011012355', 'MH-12-KB-2011', 'KTM Duke 390', '2W', 11),
('Atharv Mane', '304', '3', '9011012356', 'MH-12-AM-2012', 'Scorpio-N', '4W', 12),
('Aavya Kadam', '401', '4', '9011012357', 'MH-12-AK-2013', 'MG Astor', '4W', 13),
('Shaurya Thorat', '402', '4', '9011012358', 'MH-12-ST-2014', 'RE Classic 350', '2W', 14),
('Avni Ghadge', '403', '4', '9011012359', 'MH-12-AG-2015', 'Jeep Compass', '4W', 15),
('Arjun Nalawade', '404', '4', '9011012360', 'MH-12-AN-2016', 'Thar 4x4', '4W', 16),
('Kyra Mohite', '501', '5', '9011012361', 'MH-12-KM-2017', 'TVS Ronin', '2W', 17),
('Aryan Satav', '502', '5', '9011012362', 'MH-12-AS-2018', 'Verna Turbo', '4W', 18),
('Diya Barge', '503', '5', '9011012363', 'MH-12-DB-2019', 'Honda City', '4W', 19),
('Rudra Salunkhe', '504', '5', '9011012364', 'MH-12-RS-2020', 'Skoda Kushaq', '4W', 20),
('Siya Pol', '601', '6', '9011012365', 'MH-12-SP-2021', 'Ather 450X', 'EV', 21),
('Darsh Nikam', '602', '6', '9011012366', 'MH-12-DN-2022', 'Hyundai Creta', '4W', 22),
('Navya Ghorpade', '603', '6', '9011012367', 'MH-12-NG-2023', 'Volkswagen Taigun', '4W', 23),
('Ira Jagtap', '604', '6', '9011012368', 'MH-12-IJ-2024', 'Suzuki Gixxer', '2W', 24),
('Vansh Shitole', '701', '7', '9011012369', 'MH-12-VS-2025', 'Nissan Magnite', '4W', 25),
('Zoya Dhamal', '702', '7', '9011012370', 'MH-12-ZD-2026', 'Renault Kiger', '4W', 26),
('Kiaan Konde', '703', '7', '9011012371', 'MH-12-KK-2027', 'Tata Safari', '4W', 27),
('Shanaya Pasalkar', '704', '7', '9011012372', 'MH-12-SP-2028', 'Yamaha R15 V4', '2W', 28),
('Hridaan Jedhe', '801', '8', '9011012373', 'MH-12-HJ-2029', 'Tata Punch', '4W', 29),
('Amara Shilimkar', '802', '8', '9011012374', 'MH-12-AS-2030', 'Maruti Baleno', '4W', 30),
('Ayaan Ghule', '803', '8', '9011012375', 'MH-12-AG-2031', 'Aprilia SR 160', '2W', 31),
('Sara Magar', '804', '8', '9011012376', 'MH-12-SM-2032', 'Tata Altroz', '4W', 32),
('Ranveer Landge', '901', '9', '9011012377', 'MH-12-RL-2033', 'XUV300', '4W', 33),
('Inaya Tapkir', '902', '9', '9011012378', 'MH-12-IT-2034', 'Hyundai Venue', '4W', 34),
('Yuvan Babar', '903', '9', '9011012379', 'MH-12-YB-2035', 'Jawa Perak', '2W', 35),
('Meher Bankar', '904', '9', '9011012380', 'MH-12-MB-2036', 'Maruti Brezza', '4W', 36),
('Dhruv Misal', '1001', '10', '9011012381', 'MH-12-DM-2037', 'MG Hector', '4W', 37),
('Tara Phadtare', '1002', '10', '9011012382', 'MH-12-TP-2038', 'Vespa VXL', '2W', 38),
('Aman Jachak', '1003', '10', '9011012383', 'MH-12-AJ-2039', 'Tata Nexon', '4W', 39),
('Zian Taware', '1004', '10', '9011012384', 'MH-12-ZT-2040', 'Toyota Glanza', '4W', 40),
('Advaita Shitole', '1101', '11', '9011012385', 'MH-12-AS-2041', 'Bajaj Dominar', '2W', 41),
('Shlok Holkar', '1102', '11', '9011012386', 'MH-12-SH-2042', 'Kia Carens', '4W', 42),
('Rhea Scindia', '1103', '11', '9011012387', 'MH-12-RS-2043', 'Honda Elevate', '4W', 43),
('Kavya Malusare', '1104', '11', '9011012388', 'MH-12-KM-2044', 'KTM RC 200', '2W', 44),
('Samaira Prabhu', '1201', '12', '9011012389', 'MH-12-SP-2045', 'Maruti Fronx', '4W', 45),
('Arnav Deshpande', '1202', '12', '9011012390', 'MH-12-AD-2046', 'Hyundai i20 N Line', '4W', 46),
('Prisha Chitnis', '1203', '12', '9011012391', 'MH-12-PC-2047', 'Suzuki Burgman', '2W', 47),
('Abhiram Mujumdar', '1204', '12', '9011012392', 'MH-12-AM-2048', 'Toyota Hyryder', '4W', 48),
('Ishita Amatya', '1301', '13', '9011012393', 'MH-12-IA-2049', 'Honda Hness CB350', '2W', 49),
('Vedant Sachiv', '1302', '13', '9011012394', 'MH-12-VS-2050', 'MG ZS EV', 'EV', 50),
('Anvi Mantri', '1303', '13', '9011012395', 'MH-12-AM-2051', 'Hyundai Ioniq 5', 'EV', 51),
('Rohan Senapati', '1304', '13', '9011012396', 'MH-12-RS-2052', 'BYD Atto 3', 'EV', 52),
('Sia Sumant', '1401', '14', '9011012397', 'MH-12-SS-2053', 'TVS iQube', 'EV', 53),
('Aadi Nyayadhish', '1402', '14', '9011012398', 'MH-12-AN-2054', 'Volvo XC40 Recharge', 'EV', 54),
('Pihu Panditrao', '1403', '14', '9011012399', 'MH-12-PP-2055', 'BMW i4', 'EV', 55),
('Arav Bhosale', '1404', '14', '9011012410', 'MH-12-AB-2056', 'Ultraviolette F77', 'EV', 56),
('Kaira Gaekwad', '1501', '15', '9011012411', 'MH-12-KG-2057', 'Tiago.ev', 'EV', 57),
('Rishi Shinde', '1502', '15', '9011012412', 'MH-12-RS-2058', 'Mahindra XUV400', 'EV', 58),
('Ishan Scindia', '1503', '15', '9011012413', 'MH-12-IS-2059', 'Tork Kratos R', 'EV', 59),
('Gia Holkar', '1504', '15', '9011012414', 'MH-12-GH-2060', 'Citroen eC3', 'EV', 60),
('Tanmay Bhosale', '1601', '16', '9011012415', 'MH-12-TB-2061', 'Revolt RV400', 'EV', 61),
('Aarohi Bhosale', '1602', '16', '9011012416', 'MH-12-AB-2062', 'Kia EV6', 'EV', 62),
('Ritvik Bhosale', '1603', '16', '9011012417', 'MH-12-RB-2063', 'Audi e-tron', 'EV', 63),
('Saniya Peshwa', '1604', '16', '9011012418', 'MH-12-SP-2064', 'Mercedes EQS', 'EV', 64),
('Yash Peshwa', '1701', '17', '9011012419', 'MH-12-YP-2065', 'Porsche Taycan', 'EV', 65),
('Janhvi Peshwa', '1702', '17', '9011012420', 'MH-12-JP-2066', 'Mini Cooper SE', 'EV', 66),
('Ojas Bhau', '1703', '17', '9011012421', 'MH-12-OB-2067', 'TVS Apache RTR', '2W', 67),
('Esha Peshwa', '1704', '17', '9011012422', 'MH-12-EP-2068', 'Hero Mavrick', '2W', 68),
('Samrat Peshwa', '1801', '18', '9011012423', 'MH-12-SP-2069', 'Royal Enfield Super Meteor', '2W', 69),
('Zunaira Peshwa', '1802', '18', '9011012424', 'MH-12-ZP-2070', 'Kawasaki Ninja 300', '2W', 70);
