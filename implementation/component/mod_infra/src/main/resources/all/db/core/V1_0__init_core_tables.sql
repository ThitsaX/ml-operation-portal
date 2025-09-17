CREATE DATABASE IF NOT EXISTS `operation_portal` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `operation_portal`;

-- operation_portal.tbl_action definition

CREATE TABLE IF NOT EXISTS `tbl_action` (
  `action_id` bigint NOT NULL,
  `action_code` varchar(255) NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  `scope` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_announcement definition

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


-- operation_portal.tbl_approval_request definition

CREATE TABLE IF NOT EXISTS `tbl_approval_request` (
  `approval_request_id` bigint NOT NULL,
  `requested_action` varchar(100) NOT NULL,
  `participant_name` varchar(100) NOT NULL,
  `participant_currency` varchar(32) NOT NULL,
  `participant_currency_id` varchar(100) NOT NULL,
  `amount` decimal(20,4) DEFAULT NULL,
  `requested_by` bigint DEFAULT NULL,
  `responded_by` bigint DEFAULT NULL,
  `requested_dtm` bigint DEFAULT NULL,
  `action` varchar(100) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`approval_request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_audit definition

CREATE TABLE IF NOT EXISTS `tbl_audit` (
  `audit_id` bigint NOT NULL,
  `action_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `participant_id` bigint DEFAULT NULL,
  `input_info` text,
  `output_info` longtext,
  `exception` text,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`audit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_contact definition

CREATE TABLE IF NOT EXISTS `tbl_contact` (
  `contact_id` bigint NOT NULL,
  `participant_id` bigint DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `position` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mobile` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `contact_type` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


-- operation_portal.tbl_contact_history definition

CREATE TABLE IF NOT EXISTS `tbl_contact_history` (
  `contact_history_id` bigint NOT NULL,
  `contact_id` bigint NOT NULL,
  `participant_id` bigint NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `position` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `mobile` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `contact_type` varchar(50) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`contact_history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


-- operation_portal.tbl_extra_property definition

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


-- operation_portal.tbl_greeting definition

CREATE TABLE IF NOT EXISTS `tbl_greeting` (
  `greeting_id` bigint NOT NULL,
  `greeting_title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  `greeting_detail` text,
  `is_deleted` bit(1) DEFAULT NULL,
  `greeting_date` bigint DEFAULT NULL,
  PRIMARY KEY (`greeting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_job_execution_logs definition

CREATE TABLE IF NOT EXISTS `tbl_job_execution_logs` (
  `job_execution_log_id` bigint NOT NULL,
  `job_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime DEFAULT NULL,
  `execution_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`job_execution_log_id`),
  KEY `idx_job_name` (`job_name`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- operation_portal.tbl_liquidity_profile definition

CREATE TABLE IF NOT EXISTS `tbl_liquidity_profile` (
  `liquidity_profile_id` bigint DEFAULT NULL,
  `participant_id` bigint DEFAULT NULL,
  `bank_name` varchar(200) DEFAULT NULL,
  `account_name` varchar(200) DEFAULT NULL,
  `account_number` varchar(200) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_menu definition

CREATE TABLE IF NOT EXISTS `tbl_menu` (
  `menu_id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_participant definition

CREATE TABLE IF NOT EXISTS `tbl_participant` (
  `participant_id` bigint NOT NULL,
  `participant_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `description` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `mobile` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `logo_data_type` varchar(225) DEFAULT NULL,
  `logo` blob,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


-- operation_portal.tbl_participant_ndc definition

CREATE TABLE IF NOT EXISTS `tbl_participant_ndc` (
  `participant_ndc_id` bigint NOT NULL,
  `dfsp_code` varchar(100) DEFAULT NULL,
  `currency` varchar(100) DEFAULT NULL,
  `ndc_percent` decimal(7,4) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`participant_ndc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_participant_ndc_history definition

CREATE TABLE IF NOT EXISTS `tbl_participant_ndc_history` (
  `participant_ndc_history_id` bigint NOT NULL,
  `participant_ndc_id` bigint NOT NULL,
  `dfsp_code` varchar(100) DEFAULT NULL,
  `currency` varchar(100) DEFAULT NULL,
  `ndc_percent` decimal(7,4) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`participant_ndc_history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_principal definition

CREATE TABLE IF NOT EXISTS `tbl_principal` (
  `principal_id` bigint NOT NULL,
  `access_key` bigint DEFAULT NULL,
  `secret_key` varchar(64) DEFAULT NULL,
  `realm_id` bigint DEFAULT NULL,
  `sha_256_password_hex` varchar(64) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`principal_id`),
  UNIQUE KEY `access_key` (`access_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_role definition

CREATE TABLE IF NOT EXISTS `tbl_role` (
  `role_id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `is_dfsp` tinyint(1) DEFAULT '1',
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_scheduler_configs definition

CREATE TABLE IF NOT EXISTS `tbl_scheduler_configs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `is_active` tinyint(1) DEFAULT '1',
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- operation_portal.tbl_user definition

CREATE TABLE IF NOT EXISTS `tbl_user` (
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


-- operation_portal.tbl_blocked_action definition

CREATE TABLE IF NOT EXISTS `tbl_blocked_action` (
  `blocked_action_id` bigint NOT NULL,
  `principal_id` bigint DEFAULT NULL,
  `action_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`blocked_action_id`),
  KEY `tbl_blocked_action_tbl_action_FK` (`action_id`),
  KEY `tbl_blocked_action_tbl_user_FK` (`principal_id`),
  CONSTRAINT `tbl_blocked_action_tbl_action_FK` FOREIGN KEY (`action_id`) REFERENCES `tbl_action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_blocked_action_tbl_user_FK` FOREIGN KEY (`principal_id`) REFERENCES `tbl_principal` (`principal_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_menu_grant definition

CREATE TABLE IF NOT EXISTS `tbl_menu_grant` (
  `grant_id` bigint NOT NULL,
  `action_id` bigint DEFAULT NULL,
  `menu_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`grant_id`),
  KEY `tbl_menu_grant_tbl_action_FK` (`action_id`),
  KEY `tbl_menu_grant_tbl_menu_FK` (`menu_id`),
  CONSTRAINT `tbl_menu_grant_tbl_action_FK` FOREIGN KEY (`action_id`) REFERENCES `tbl_action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_menu_grant_tbl_menu_FK` FOREIGN KEY (`menu_id`) REFERENCES `tbl_menu` (`menu_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_principal_grant definition

CREATE TABLE IF NOT EXISTS `tbl_principal_grant` (
  `grant_id` bigint NOT NULL,
  `principal_id` bigint DEFAULT NULL,
  `action_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`grant_id`),
  KEY `tbl_principal_grant_tbl_principal_FK` (`principal_id`),
  KEY `tbl_principal_grant_tbl_action_FK` (`action_id`),
  CONSTRAINT `tbl_principal_grant_tbl_action_FK` FOREIGN KEY (`action_id`) REFERENCES `tbl_action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_principal_grant_tbl_principal_FK` FOREIGN KEY (`principal_id`) REFERENCES `tbl_principal` (`principal_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_principal_role definition

CREATE TABLE IF NOT EXISTS `tbl_principal_role` (
  `principal_role_id` bigint NOT NULL,
  `role_id` bigint DEFAULT NULL,
  `principal_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`principal_role_id`),
  KEY `tbl_principal_role_tbl_principal_FK` (`principal_id`),
  KEY `tbl_principal_role_tbl_role_FK` (`role_id`),
  CONSTRAINT `tbl_principal_role_tbl_principal_FK` FOREIGN KEY (`principal_id`) REFERENCES `tbl_principal` (`principal_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_principal_role_tbl_role_FK` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- operation_portal.tbl_role_grant definition

CREATE TABLE IF NOT EXISTS `tbl_role_grant` (
  `grant_id` bigint NOT NULL,
  `role_id` bigint DEFAULT NULL,
  `action_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`grant_id`),
  KEY `tbl_role_grant_tbl_action_FK` (`action_id`),
  KEY `tbl_role_grant_tbl_role_FK` (`role_id`),
  CONSTRAINT `tbl_role_grant_tbl_action_FK` FOREIGN KEY (`action_id`) REFERENCES `tbl_action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_role_grant_tbl_role_FK` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- Init Data

INSERT INTO tbl_participant (participant_id,participant_name,description,address,mobile,logo_data_type,logo,created_date,updated_date) VALUES
	 (1111111111111111,'hub','Hub Operator',NULL,NULL,NULL,NULL,1758083048,1758083048);

INSERT INTO tbl_user (user_id,participant_id,name,email,first_name,last_name,job_title,created_date,updated_date,is_deleted) VALUES
	 (1111111111111111,1111111111111111,'thitsaworks','hub.user@thitsaworks.com','Thitsaworks','User','Hub Operator',1758083048,1758083048,0);

INSERT INTO tbl_principal (principal_id,access_key,secret_key,realm_id,sha_256_password_hex,status,created_date,updated_date) VALUES
	 (1111111111111111,411194012689530880,'ea3184c0-0c70-4ab5-af24-adb3ac3b6885',1111111111111111,'8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92','ACTIVE',0,NULL);

INSERT INTO tbl_role (role_id,name,active,is_dfsp,created_date,updated_date) VALUES
	 (1,'HUB-Admin',1,0,1758083048,1758083048),
	 (2,'HUB-Manager',1,0,1758083048,1758083048),
	 (3,'HUB-Operator',1,0,1758083048,1758083048),
	 (4,'HUB-User',1,0,1758083048,1758083048),
	 (5,'DFSP-Admin',1,1,1758083048,1758083048),
	 (6,'DFSP-Operation',1,1,1758083048,1758083048);

INSERT INTO tbl_principal_role (principal_role_id,role_id,principal_id,created_date,updated_date) VALUES
	 (1,1,1111111111111111,1758083048,1758083048);

INSERT INTO tbl_role_grant (grant_id,role_id,action_id,created_date,updated_date) VALUES
	 (1,1,1,1758083048,1758083048);

INSERT INTO tbl_action (action_id,action_code,created_date,updated_date,`scope`,description) VALUES
	 (1,'GrantRoleActions',1757501222,1757501229,'OPERATION_PORTAL','Automatically registered action for use case: GrantRoleActionsHandler');

INSERT INTO tbl_menu (menu_id,name,parent_id,is_active,created_date,updated_date) VALUES
	 (1,'Operational Portal','0',1,1757501222,1757501222),
	 (2,'Home','1',1,1757501222,1757501222),
	 (3,'Participants','1',1,1757501222,1757501222),
	 (4,'Participant Positions','3',1,1757501222,1757501222),
	 (5,'Participant','3',1,1757501222,1757501222),
	 (6,'User Management','1',1,1757501222,1757501222),
	 (7,'Users','6',1,1757501222,1757501222),
	 (8,'Transfers','1',1,1757501222,1757501222),
	 (9,'Settlement','1',1,1757501222,1757501222),
	 (10,'Settlement Models','9',1,1757501222,1757501222);
INSERT INTO tbl_menu (menu_id,name,parent_id,is_active,created_date,updated_date) VALUES
	 (11,'Settlement Windows','9',1,1757501222,1757501222),
	 (12,'Finalize Settlement','9',1,1757501222,1757501222),
	 (13,'Reports','1',1,1757501222,1757501222),
	 (14,'Settlement Bank Report','13',1,1757501222,1757501222),
	 (15,'Settlement Detail Report','13',1,1757501222,1757501222),
	 (16,'Settlement Summary Report','13',1,1757501222,1757501222),
	 (17,'Settlement Statement Report','13',1,1757501222,1757501222),
	 (18,'Settlement Audit Report','13',1,1757501222,1757501222),
	 (19,'Audit Report','13',1,1757501222,1757501222),
	 (20,'Pending Approvals','1',1,1757501222,1757501222);
INSERT INTO tbl_menu (menu_id,name,parent_id,is_active,created_date,updated_date) VALUES
	 (21,'Audit','1',1,1757501222,1757501222),
	 (22,'Support Center','1',1,1757501222,1757501222);
