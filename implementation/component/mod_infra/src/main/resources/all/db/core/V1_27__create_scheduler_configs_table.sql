CREATE TABLE IF NOT EXISTS tbl_scheduler_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    cron_expression VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_date bigint DEFAULT NULL,
    updated_date bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample schedules
INSERT INTO tbl_scheduler_configs (name, cron_expression, description, is_active)
VALUES 
('Every 20 seconds on weekdays', '*/20 * * ? * MON-FRI', 'Runs every 20 seconds on weekdays', true),
('Every 7 seconds on weekdays', '*/7 * * ? * MON-FRI', 'Runs every 7 seconds on weekdays', true);
