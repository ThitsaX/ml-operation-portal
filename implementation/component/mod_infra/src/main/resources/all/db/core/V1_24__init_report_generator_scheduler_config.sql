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
    job_name = VALUES(job_name),
    description = VALUES(description),
    cron_expression = VALUES(cron_expression),
    zone_id = VALUES(zone_id),
    is_active = VALUES(is_active),
    updated_date = VALUES(updated_date);
