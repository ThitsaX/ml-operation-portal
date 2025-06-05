-- dfsp_portal.tbl_liquidity_profile definition

CREATE TABLE `tbl_liquidity_profile` (
  `liquidity_profile_id` bigint DEFAULT NULL,
  `participant_id` bigint DEFAULT NULL,
  `account_name` varchar(200) DEFAULT NULL,
  `account_number` varchar(200) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;