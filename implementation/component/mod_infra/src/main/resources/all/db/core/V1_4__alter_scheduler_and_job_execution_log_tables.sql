RENAME TABLE tbl_scheduler_configs TO tbl_scheduler_config;
ALTER TABLE tbl_scheduler_config CHANGE id scheduler_config_id bigint NOT NULL;
ALTER TABLE tbl_scheduler_config ADD job_name varchar(100) NOT NULL AFTER name;
ALTER TABLE tbl_scheduler_config CHANGE cron_expression cron_expression varchar(100) NOT NULL AFTER description;
ALTER TABLE tbl_scheduler_config ADD zone_id varchar(50) NOT NULL AFTER cron_expression;

RENAME TABLE tbl_job_execution_logs TO tbl_job_execution_log;


