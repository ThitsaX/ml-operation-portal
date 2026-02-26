ALTER TABLE tbl_participant ADD parent_participant_name varchar(256) NULL;
ALTER TABLE tbl_participant CHANGE parent_participant_name parent_participant_name varchar(256) NULL AFTER participant_name;
