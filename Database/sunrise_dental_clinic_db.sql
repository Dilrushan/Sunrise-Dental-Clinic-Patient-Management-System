-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Sep 04, 2026 at 09:00 PM
-- Server version: 8.2.0
-- PHP Version: 8.2.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sunrise_dental_clinic_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
CREATE TABLE IF NOT EXISTS `appointments` (
  `appointment_id` int NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(100) NOT NULL,
  `contact_no` varchar(15) NOT NULL,
  `doctor_id` int NOT NULL,
  `appointment_date` varchar(20) NOT NULL,
  `visit_type` varchar(50) NOT NULL,
  `treatment_prescribed` varchar(200) DEFAULT NULL,
  `fee` decimal(10,2) DEFAULT '0.00',
  `status` varchar(20) DEFAULT 'Scheduled',
  PRIMARY KEY (`appointment_id`),
  KEY `doctor_id` (`doctor_id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`appointment_id`, `patient_name`, `contact_no`, `doctor_id`, `appointment_date`, `visit_type`, `treatment_prescribed`, `fee`, `status`) VALUES
(4, 'Vidyan', '0777666356', 6, '2026-10-03', 'General', 'Root Canal Treatment (LKR 8000)', 8000.00, 'Scheduled'),
(13, 'Kamal Perera', '0787575201', 1, '2026-12-01', 'General Consultation / Checkup (LKR 0)', NULL, 0.00, 'Scheduled'),
(14, 'Kamal Perera', '0787575201', 2, '2026-10-01', 'General', 'General Consultation / Checkup (LKR 0)', 0.00, 'Scheduled'),
(16, 'Nimal Silva', '0755675456', 1, '2026-11-15', 'General Consultation / Checkup (LKR 0)', NULL, 0.00, 'Scheduled');

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
CREATE TABLE IF NOT EXISTS `patients` (
  `patient_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `contact_number` varchar(15) NOT NULL,
  `email` varchar(100) NOT NULL,
  `treatment_history` text,
  PRIMARY KEY (`patient_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`patient_id`, `full_name`, `address`, `contact_number`, `email`, `treatment_history`) VALUES
(1, 'Bheem', 'bamba', '0112356356', 'bheem@gmail.com', ''),
(2, 'Vidyan', 'Colombo-06', '0777666356', 'vidyan@gmail.com', ''),
(3, 'Vinay', 'Mumbai', '+91200365365', 'vinay@gmail.com', ''),
(4, 'Kamal Perera', 'Colombo-06', '0787575201', 'kamalp@gmail.com', ''),
(5, 'Nimal Silva', 'Kandy', '0755675456', 'Nimal@gmail.com', '');

-- --------------------------------------------------------

--
-- Table structure for table `treatments`
--

DROP TABLE IF EXISTS `treatments`;
CREATE TABLE IF NOT EXISTS `treatments` (
  `treatment_id` int NOT NULL AUTO_INCREMENT,
  `treatment_name` varchar(200) NOT NULL,
  `fee` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`treatment_id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `treatments`
--

INSERT INTO `treatments` (`treatment_id`, `treatment_name`, `fee`) VALUES
(1, 'Routine Teeth Cleaning & Polishing (LKR 1500)', 1500.00),
(2, 'Composite Dental Filling (LKR 2500)', 2500.00),
(3, 'Root Canal Treatment (LKR 8000)', 8000.00),
(4, 'Tooth Extraction (LKR 3500)', 3500.00),
(5, 'Dental Crown Fitting (LKR 15000)', 15000.00),
(6, 'General Consultation / Checkup (LKR 0)', 0.00);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role` enum('Admin','Receptionist','Doctor') NOT NULL,
  `status` enum('Active','Inactive') DEFAULT 'Active',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password_hash`, `full_name`, `role`, `status`) VALUES
(1, 'admin', 'admin123', 'System Administrator', 'Admin', 'Active'),
(2, 'drsmith', 'doctor123', 'Dr. John Smith', 'Doctor', 'Active'),
(3, 'reception1', 'recept123', 'Jane Receptionist', 'Receptionist', 'Active'),
(4, 'shiva', '12345678', 'shiva', 'Receptionist', 'Active'),
(5, 'Vivek', '12345678', 'Vivek', 'Receptionist', 'Active'),
(6, 'Dr.Thivi', '12345678', 'Dr.Thivi', 'Doctor', 'Active'),
(8, 'newdoctor', 'doctor123', 'newdoctor', 'Doctor', 'Active'),
(9, 'recept02', 'recept1234', 'recept02', 'Receptionist', 'Active'),
(10, 'admin02', 'admin1234', 'admin02', 'Admin', 'Active');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
