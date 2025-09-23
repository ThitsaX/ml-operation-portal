ALTER TABLE tbl_participant_ndc CHANGE dfsp_code participant_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE tbl_participant_ndc_history CHANGE dfsp_code participant_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
