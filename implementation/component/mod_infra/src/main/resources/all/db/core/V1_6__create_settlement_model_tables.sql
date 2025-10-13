-- operation_portal.tbl_settlement_model

CREATE TABLE IF NOT EXISTS `tbl_settlement_model` (
  `settlement_model_id` bigint NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `currency_id` varchar(3) DEFAULT NULL,
  `is_active` bit(1) DEFAULT NULL,
  `auto_close_window` bit(1) DEFAULT 0,
  `require_liquidity_check` bit(1) DEFAULT NULL,
  `auto_position_reset` bit(1) DEFAULT NULL,
  `adjust_position` bit(1) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`settlement_model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- operation_portal.tbl_settlement_model

CREATE TABLE IF NOT EXISTS `tbl_settlement_scheduler_config` (
  `settlement_model_id` bigint NOT NULL,
  `scheduler_config_id` bigint NOT NULL,
  PRIMARY KEY (`settlement_model_id`),
  UNIQUE KEY `tbl_settlement_scheduler_config_mapping_un` (`settlement_model_id`,`scheduler_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;