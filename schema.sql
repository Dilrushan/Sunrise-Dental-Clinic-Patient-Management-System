-- =============================================
-- Sunrise Dental Clinic Patient Management DB
-- =============================================

DROP DATABASE IF EXISTS sunrise_dental_clinic_db;
CREATE DATABASE sunrise_dental_clinic_db;
USE sunrise_dental_clinic_db;

-- -------------------------------------------
-- 1. USERS TABLE (Staff: Admin, Receptionist, Doctor)
-- -------------------------------------------
CREATE TABLE users (
    user_id       INT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100) NOT NULL,
    role          ENUM('Admin', 'Receptionist', 'Doctor') NOT NULL,
    status        ENUM('Active', 'Inactive') DEFAULT 'Active'
);

-- -------------------------------------------
-- 2. PATIENTS TABLE
-- -------------------------------------------
CREATE TABLE patients (
    patient_id      INT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100) NOT NULL,
    address         VARCHAR(255),
    contact_number  VARCHAR(15)  NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    treatment_history TEXT
);

-- -------------------------------------------
-- 3. TREATMENTS TABLE (Fee lookup reference)
-- -------------------------------------------
CREATE TABLE treatments (
    treatment_id   INT AUTO_INCREMENT PRIMARY KEY,
    treatment_name VARCHAR(200) NOT NULL,
    fee            DECIMAL(10,2) NOT NULL DEFAULT 0.00
);

-- -------------------------------------------
-- 4. APPOINTMENTS TABLE
-- -------------------------------------------
CREATE TABLE appointments (
    appointment_id      INT AUTO_INCREMENT PRIMARY KEY,
    patient_name        VARCHAR(100)  NOT NULL,
    contact_no          VARCHAR(15)   NOT NULL,
    doctor_id           INT           NOT NULL,
    appointment_date    VARCHAR(20)   NOT NULL,
    visit_type          VARCHAR(50)   NOT NULL,
    treatment_prescribed VARCHAR(200) DEFAULT NULL,
    fee                 DECIMAL(10,2) DEFAULT 0.00,
    status              VARCHAR(20)   DEFAULT 'Scheduled',
    FOREIGN KEY (doctor_id) REFERENCES users(user_id)
);

-- -------------------------------------------
-- SEED DATA: Default treatment options
-- -------------------------------------------
INSERT INTO treatments (treatment_name, fee) VALUES
('Routine Teeth Cleaning & Polishing (LKR 1500)',  1500.00),
('Composite Dental Filling (LKR 2500)',            2500.00),
('Root Canal Treatment (LKR 8000)',                8000.00),
('Tooth Extraction (LKR 3500)',                    3500.00),
('Dental Crown Fitting (LKR 15000)',               15000.00),
('General Consultation / Checkup (LKR 0)',         0.00);

-- -------------------------------------------
-- SEED DATA: Default admin/doctor/receptionist
-- -------------------------------------------
INSERT INTO users (username, password_hash, full_name, role, status) VALUES
('admin',      'admin123',  'System Administrator', 'Admin',       'Active'),
('drsmith',    'doctor123', 'Dr. John Smith',       'Doctor',      'Active'),
('reception1', 'recept123', 'Jane Receptionist',    'Receptionist', 'Active');
