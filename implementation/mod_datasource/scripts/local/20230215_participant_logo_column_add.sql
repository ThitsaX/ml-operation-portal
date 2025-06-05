
DROP TABLE IF EXISTS `tbl_participant`;
CREATE TABLE `tbl_participant` (
  `participant_id` bigint NOT NULL,
  `dfsp_code` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `address` varchar(500) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  `business_contact_id` bigint DEFAULT NULL,
  `technical_contact_id` bigint DEFAULT NULL,
  `logoDataType` varchar(225) DEFAULT NULL,
  `logoBase64` blob,
  PRIMARY KEY (`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
