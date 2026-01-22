ALTER TABLE tbl_audit ADD request_id BIGINT NULL;
ALTER TABLE tbl_audit CHANGE request_id request_id BIGINT NULL AFTER participant_id;
