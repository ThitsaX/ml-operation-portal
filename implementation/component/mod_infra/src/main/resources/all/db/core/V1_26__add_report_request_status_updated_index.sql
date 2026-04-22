CREATE INDEX idx_tbl_report_request_status_updated
    ON tbl_report_request (status, updated_date);
