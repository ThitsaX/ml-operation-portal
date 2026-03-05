CREATE TABLE `tbl_report_request` (
    `request_id` BIGINT NOT NULL,
    `report_type` VARCHAR(64) NOT NULL,
    `params_signature` CHAR(64) NOT NULL,
    `data_version` DATE NOT NULL,
    `status` VARCHAR(16) NOT NULL,
    `file_type` VARCHAR(16) DEFAULT NULL,
    `file_url` VARCHAR(512) DEFAULT NULL,
    `error_message` VARCHAR(1000) DEFAULT NULL,
    `created_date` BIGINT DEFAULT NULL,
    `updated_date` BIGINT DEFAULT NULL,
    `finished_date` BIGINT DEFAULT NULL,
    PRIMARY KEY (`request_id`),
    UNIQUE KEY `uk_tbl_report_request_dedupe` (`report_type`,`params_signature`,`data_version`),
    KEY `idx_tbl_report_request_status_created` (`status`,`created_date`)
);

CREATE TABLE `tbl_report_request_param` (
    `request_param_id` BIGINT NOT NULL,
    `request_id` BIGINT NOT NULL,
    `param_key` VARCHAR(128) NOT NULL,
    `param_value` TEXT,
    `created_date` BIGINT DEFAULT NULL,
    `updated_date` BIGINT DEFAULT NULL,
    PRIMARY KEY (`request_param_id`),
    KEY `idx_tbl_report_request_param_request_id` (`request_id`),
    CONSTRAINT `fk_tbl_report_request_param_request_id`
        FOREIGN KEY (`request_id`) REFERENCES `tbl_report_request` (`request_id`)
        ON DELETE CASCADE
);
