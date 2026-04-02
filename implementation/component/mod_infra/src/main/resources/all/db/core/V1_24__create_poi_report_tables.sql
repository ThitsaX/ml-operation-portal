CREATE TABLE IF NOT EXISTS `tbl_report_request` (
    `request_id`      BIGINT NOT NULL,
    `report_type`     VARCHAR(64) NOT NULL,
    `params_signature` CHAR(64) NOT NULL,
    `status`          VARCHAR(16) NOT NULL,
    `file_type`       VARCHAR(16) DEFAULT NULL,
    `file_url`        VARCHAR(512) DEFAULT NULL,
    `error_message`   VARCHAR(1000) DEFAULT NULL,
    `created_date`    BIGINT DEFAULT NULL,
    `updated_date`    BIGINT DEFAULT NULL,
    `finished_date`   BIGINT DEFAULT NULL,
    PRIMARY KEY (`request_id`),
    KEY `idx_tbl_report_request_status_created` (`status`, `created_date`)
);

CREATE TABLE IF NOT EXISTS `tbl_report_request_param` (
    `request_param_id` BIGINT NOT NULL,
    `request_id`       BIGINT NOT NULL,
    `param_key`        VARCHAR(128) NOT NULL,
    `param_value`      TEXT,
    `created_date`     BIGINT DEFAULT NULL,
    `updated_date`     BIGINT DEFAULT NULL,
    PRIMARY KEY (`request_param_id`),
    KEY `idx_tbl_report_request_param_request_id` (`request_id`),
    CONSTRAINT `fk_tbl_report_request_param_request_id`
        FOREIGN KEY (`request_id`) REFERENCES `tbl_report_request` (`request_id`)
        ON DELETE CASCADE
);

INSERT INTO tbl_scheduler_config
    (scheduler_config_id, name, job_name, description, cron_expression, zone_id, is_active, created_date, updated_date)
VALUES
    (1111111111111112,
     'ReportGeneratorSync',
     'ReportGeneratorScheduler',
     'Executes every 5 seconds to process pending report download requests.',
     '*/5 * * * * *',
     '+00:00',
     1,
     UNIX_TIMESTAMP(),
     UNIX_TIMESTAMP())
ON DUPLICATE KEY UPDATE
    job_name        = VALUES(job_name),
    description     = VALUES(description),
    cron_expression = VALUES(cron_expression),
    zone_id         = VALUES(zone_id),
    is_active       = VALUES(is_active),
    updated_date    = VALUES(updated_date);