-- dfsp_portal.tbl_principal definition
DROP TABLE IF EXISTS `tbl_principal`;
CREATE TABLE `tbl_principal` (
  `principal_id` bigint NOT NULL,
  `access_key` bigint DEFAULT NULL,
  `secret_key` varchar(64) DEFAULT NULL,
  `realm` varchar(10) DEFAULT NULL,
  `realm_id` bigint DEFAULT NULL,
  `sha_256_password_hex` varchar(64) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`principal_id`), UNIQUE KEY (`access_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;