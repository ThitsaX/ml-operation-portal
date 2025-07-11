
CREATE TABLE IF NOT EXISTS `tbl_approval_request` (
`approval_request_id` BIGINT NOT NULL,
`requested_action` varchar(100) NOT NULL,
`dfsp` varchar(100) NOT NULL,
`currency` varchar(32) NOT NULL,
`amount` DECIMAL(20,4) NULL,
`requested_by` BIGINT NULL,
`responded_by` BIGINT NULL,
`requested_dtm` BIGINT NULL,
`action` varchar(100) NULL,
`created_date` bigint NULL,
`updated_date` bigint NULL,
CONSTRAINT tbl_approval_request_pk PRIMARY KEY (approval_request_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
