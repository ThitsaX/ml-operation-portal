ALTER TABLE `tbl_report_request`
    ADD COLUMN `retry_count` INT NOT NULL DEFAULT 0 AFTER `error_message`;

