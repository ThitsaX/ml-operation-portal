-- iam_action definition

CREATE TABLE `iam_action` (
  `action_id` bigint NOT NULL,
  `action_code` varchar(255) NOT NULL,
  `scope` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- tbl_role definition

CREATE TABLE `tbl_role` (
  `role_id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- tbl_user definition

CREATE TABLE `tbl_user` (
  `user_id` bigint NOT NULL,
  `access_key` bigint DEFAULT NULL,
  `secret_key` varchar(64) DEFAULT NULL,
  `realm` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `realm_id` bigint DEFAULT NULL,
  `sha_256_password_hex` varchar(64) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- tbl_blocked_action definition

CREATE TABLE `tbl_blocked_action` (
  `blocked_action_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `action_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`blocked_action_id`),
  KEY `tbl_blocked_action_iam_action_FK` (`action_id`),
  KEY `tbl_blocked_action_tbl_user_FK` (`user_id`),
  CONSTRAINT `tbl_blocked_action_iam_action_FK` FOREIGN KEY (`action_id`) REFERENCES `iam_action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_blocked_action_tbl_user_FK` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- tbl_role_grant definition

CREATE TABLE `tbl_role_grant` (
  `grant_id` bigint NOT NULL,
  `role_id` bigint DEFAULT NULL,
  `action_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`grant_id`),
  KEY `tbl_role_grant_iam_action_FK` (`action_id`),
  KEY `tbl_role_grant_tbl_role_FK` (`role_id`),
  CONSTRAINT `tbl_role_grant_iam_action_FK` FOREIGN KEY (`action_id`) REFERENCES `iam_action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_role_grant_tbl_role_FK` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- tbl_user_grant definition

CREATE TABLE `tbl_user_grant` (
  `grant_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `action_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`grant_id`),
  KEY `tbl_user_grant_tbl_user_FK` (`user_id`),
  KEY `tbl_user_grant_iam_action_FK` (`action_id`),
  CONSTRAINT `tbl_user_grant_iam_action_FK` FOREIGN KEY (`action_id`) REFERENCES `iam_action` (`action_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_user_grant_tbl_user_FK` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- tbl_user_role definition

CREATE TABLE `tbl_user_role` (
  `user_role_id` bigint NOT NULL,
  `role_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`user_role_id`),
  KEY `tbl_user_role_tbl_user_FK` (`user_id`),
  KEY `tbl_user_role_tbl_role_FK` (`role_id`),
  CONSTRAINT `tbl_user_role_tbl_role_FK` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tbl_user_role_tbl_user_FK` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;