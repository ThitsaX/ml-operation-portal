ALTER TABLE tbl_participant CHANGE dfsp_code participant_name varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL;
ALTER TABLE tbl_participant CHANGE name description varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL;
ALTER TABLE tbl_participant DROP COLUMN dfsp_name;
