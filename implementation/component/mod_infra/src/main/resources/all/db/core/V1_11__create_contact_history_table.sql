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
