ALTER TABLE tbl_audit ADD `exception` text NULL;
ALTER TABLE tbl_audit CHANGE `exception` `exception` text NULL AFTER output_info;
