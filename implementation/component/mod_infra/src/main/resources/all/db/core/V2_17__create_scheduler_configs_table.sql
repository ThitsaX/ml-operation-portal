CREATE TABLE IF NOT EXISTS scheduler_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    cron_expression VARCHAR(100) NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default schedules
INSERT INTO scheduler_configs (name, cron_expression, description, is_active) 
VALUES 
('Every 20 seconds on weekdays', '*/20 * * ? * MON-FRI', 'Runs every 20 seconds on weekdays', true),
('Every 7 seconds on weekdays', '*/7 * * ? * MON-FRI', 'Runs every 7 seconds on weekdays', true)
ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;
