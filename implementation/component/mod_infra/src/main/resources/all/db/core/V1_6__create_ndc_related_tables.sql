CREATE TABLE `tbl_participant_ndc` (
  `participant_ndc_id` bigint NOT NULL,
  `dfsp_code` varchar(100) DEFAULT NULL,
  `currency` varchar(100) DEFAULT NULL,
  `ndc_percent` decimal(5,4) DEFAULT NULL,
  `ndc_amount` decimal(5,4) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`participant_ndc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tbl_participant_ndc_history` (
  `participant_ndc_history_id` bigint NOT NULL,
  `participant_ndc_id` bigint NOT NULL,
  `dfsp_code` varchar(100) DEFAULT NULL,
  `currency` varchar(100) DEFAULT NULL,
  `ndc_percent` decimal(5,4) DEFAULT NULL,
  `ndc_amount` decimal(5,4) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`participant_ndc_history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
