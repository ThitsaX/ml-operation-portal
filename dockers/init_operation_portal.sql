-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dfsp_portal
-- ------------------------------------------------------
-- Server version	8.0.34

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Write credentials to a file
CREATE USER IF NOT EXISTS 'dfsp_portal_jdbc'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'dfsp_portal_jdbc'@'%';
CREATE USER IF NOT EXISTS 'dfsp_portal_ro'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'dfsp_portal_ro'@'%';

-- Dumping database structure for central_ledger
CREATE DATABASE IF NOT EXISTS `operation_portal` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `operation_portal`;
--
-- Table structure for table `tbl_action`
--

CREATE TABLE IF NOT EXISTS `tbl_action` (
`action_id` bigint NOT NULL,
`name` varchar(150) DEFAULT NULL,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `tbl_announcement` (
`announcement_id` bigint NOT NULL,
`announcement_title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
`announcement_date` bigint DEFAULT NULL,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
`announcement_detail` text,
`is_deleted` bit(1) DEFAULT NULL,
PRIMARY KEY (`announcement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `tbl_audit` (
`audit_id` bigint NOT NULL,
`action_id` bigint NOT NULL,
`user_id` bigint NOT NULL,
`participant_id` bigint,
`input_info` text,
`output_info` text,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
PRIMARY KEY (`audit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `tbl_contact` (
`contact_id` bigint NOT NULL,
`participant_id` bigint DEFAULT NULL,
`name` varchar(200) DEFAULT NULL,
`title` varchar(100) DEFAULT NULL,
`email` varchar(100) DEFAULT NULL,
`mobile` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `tbl_extra_property` (
`extra_property_id` bigint NOT NULL,
`participant_id` bigint DEFAULT NULL,
`property_key` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
`label` varchar(100) DEFAULT NULL,
`property_value` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
PRIMARY KEY (`extra_property_id`),
UNIQUE KEY `participant_id_property_key` (`participant_id`,`property_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `tbl_hub_user` (
`user_id` bigint NOT NULL,
`name` varchar(50) DEFAULT NULL,
`email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
`first_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
`last_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
`job_title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
`is_deleted` bit(1) DEFAULT NULL,
PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `tbl_liquidity_profile` (
`liquidity_profile_id` bigint DEFAULT NULL,
`participant_id` bigint DEFAULT NULL,
`account_name` varchar(200) DEFAULT NULL,
`account_number` varchar(200) DEFAULT NULL,
`currency` varchar(10) DEFAULT NULL,
`is_active` bit(1) DEFAULT NULL,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `tbl_participant` (
`participant_id` bigint NOT NULL,
`dfsp_code` varchar(100) DEFAULT NULL,
`name` varchar(100) DEFAULT NULL,
`dfsp_name` varchar(100) DEFAULT NULL,
`address` varchar(500) DEFAULT NULL,
`mobile` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
`business_contact_id` bigint DEFAULT NULL,
`technical_contact_id` bigint DEFAULT NULL,
PRIMARY KEY (`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `tbl_participant_user` (
`user_id` bigint NOT NULL,
`participant_id` bigint DEFAULT NULL,
`name` varchar(100) DEFAULT NULL,
`email` varchar(100) DEFAULT NULL,
`first_name` text,
`last_name` text,
`job_title` text,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
`is_deleted` bit(1) DEFAULT NULL,
PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE IF NOT EXISTS `tbl_principal` (
`principal_id` bigint NOT NULL,
`access_key` bigint DEFAULT NULL,
`secret_key` varchar(64) DEFAULT NULL,
`realm` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
`realm_id` bigint DEFAULT NULL,
`sha_256_password_hex` varchar(64) DEFAULT NULL,
`status` varchar(10) DEFAULT NULL,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
`user_role_type` varchar(100) DEFAULT NULL,
PRIMARY KEY (`principal_id`),
UNIQUE KEY `access_key` (`access_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `tbl_principal` (`principal_id`, `access_key`, `secret_key`, `realm`, `realm_id`, `sha_256_password_hex`, `status`, `created_date`, `updated_date`, `user_role_type`) VALUES
(1111111111111111, 411194012689530880, 'ea3184c0-0c70-4ab5-af24-adb3ac3b6885', 'HUB_OPERATOR', NULL, '8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92', 'ACTIVE', 0, NULL, 'ADMIN');

INSERT INTO `tbl_hub_user` (`user_id`, `name`, `email`, `first_name`, `last_name`, `job_title`, `created_date`, `updated_date`, `is_deleted`) VALUES
(1111111111111111,  'thitsaworks', 'hub.user@thitsaworks.com', 'Thitsaworks', 'User', 'Hub Operator', 1693805228, 1693805228, b'0');


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-05 19:43:45
