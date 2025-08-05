-- menu definition

CREATE TABLE `tbl_menu` (
  `menu_id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id`  varchar(255) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- menu_grant definition

CREATE TABLE `tbl_menu_grant` (
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

