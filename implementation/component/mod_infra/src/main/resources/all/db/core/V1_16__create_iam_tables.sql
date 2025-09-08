ALTER TABLE tbl_principal DROP COLUMN user_role_type;

ALTER TABLE tbl_action CHANGE COLUMN name action_code VARCHAR(255) NOT NULL;

ALTER TABLE tbl_action ADD COLUMN scope VARCHAR(255) NOT NULL, ADD COLUMN description VARCHAR(255) DEFAULT NULL;


-- tbl_role definition

CREATE TABLE `tbl_role` (
  `role_id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- tbl_blocked_action definition

CREATE TABLE `tbl_blocked_action` (
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


-- tbl_role_grant definition

CREATE TABLE `tbl_role_grant` (
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


-- tbl_user_grant definition

CREATE TABLE `tbl_principal_grant` (
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


-- tbl_user_role definition

CREATE TABLE `tbl_principal_role` (
  `principal_role_id` bigint NOT NULL,
  `role_id` bigint DEFAULT NULL,
  `principal_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`principal_role_id`),
  KEY `tbl_principal_role_tbl_principal_FK` (`principal_id`),
  KEY `tbl_principal_role_tbl_role_FK` (`role_id`),
  CONSTRAINT `tbl_principal_role_tbl_role_FK` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_principal_role_tbl_principal_FK` FOREIGN KEY (`principal_id`) REFERENCES `tbl_principal` (`principal_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;