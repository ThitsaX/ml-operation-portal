INSERT INTO tbl_scheduler_config (scheduler_config_id,name,job_name,cron_expression,description,is_active,created_date,updated_date)
	VALUES (1111111111111111,'DailyOldAnnouncementsCleanup','RemovePastSixMonthsAnnouncementsScheduler','0 0 0 * * ?','Executes daily at midnight to clean up announcements older than 6 months.',1,1758083048,1758083048);
