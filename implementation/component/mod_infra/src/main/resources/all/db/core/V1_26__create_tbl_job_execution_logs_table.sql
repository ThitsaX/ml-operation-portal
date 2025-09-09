-- Create job execution logs table with final schema
-- This replaces the previous V1_16 and V1_17 migrations

CREATE TABLE IF NOT EXISTS tbl_job_execution_logs (
    job_execution_log_id BIGINT PRIMARY KEY,
    job_name VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    execution_message TEXT,
    created_date bigint DEFAULT NULL,
    updated_date bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes
CREATE INDEX idx_job_name ON tbl_job_execution_logs(job_name);
CREATE INDEX idx_status ON tbl_job_execution_logs(status);
CREATE INDEX idx_start_time ON tbl_job_execution_logs(start_time);
