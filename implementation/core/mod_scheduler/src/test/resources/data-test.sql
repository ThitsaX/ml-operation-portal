-- Insert test data for job execution logs
INSERT INTO tbl_job_execution_logs (job_execution_log_id, job_name, status, start_time, end_time, execution_message)
VALUES 
    (1001, 'testJob1', 'COMPLETED', NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 55 MINUTE, 'Job completed successfully'),
    (1002, 'testJob1', 'FAILED', NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 1 HOUR, 'Job failed: Connection timeout'),
    (1003, 'testJob2', 'COMPLETED', NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 2 HOUR, 'Job completed successfully'),
    (1004, 'testJob3', 'RUNNING', NOW() - INTERVAL 30 MINUTE, NULL, 'Job is running'),
    (1005, 'testJob1', 'COMPLETED', NOW() - INTERVAL 4 HOUR, NOW() - INTERVAL 3 HOUR, 'Job completed successfully');
