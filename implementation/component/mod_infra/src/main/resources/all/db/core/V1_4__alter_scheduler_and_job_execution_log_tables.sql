RENAME TABLE tbl_scheduler_configs TO tbl_scheduler_config;
ALTER TABLE tbl_scheduler_config CHANGE id scheduler_config_id bigint NOT NULL;
ALTER TABLE tbl_scheduler_config ADD job_name varchar(100) NOT NULL AFTER name;

RENAME TABLE tbl_job_execution_logs TO tbl_job_execution_log;


