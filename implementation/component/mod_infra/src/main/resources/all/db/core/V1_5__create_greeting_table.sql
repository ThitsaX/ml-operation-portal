CREATE TABLE IF NOT EXISTS `tbl_greeting` (
`greeting_id` bigint NOT NULL,
`greeting_title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
`created_date` bigint DEFAULT NULL,
`updated_date` bigint DEFAULT NULL,
`greeting_detail` text,
PRIMARY KEY (`greeting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;