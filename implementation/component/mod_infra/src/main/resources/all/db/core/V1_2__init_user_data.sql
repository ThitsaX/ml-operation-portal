INSERT INTO tbl_user (user_id,participant_id,name,email,first_name,last_name,job_title,created_date,updated_date,is_deleted) VALUES
	 (1111111111111112,1111111111111111,'thitsaworks','twadmin@email.com','Thitsaworks','User','Hub Operator',1758083048,1758083048,0);

INSERT INTO tbl_principal (principal_id,access_key,secret_key,realm_id,sha_256_password_hex,status,created_date,updated_date) VALUES
	 (1111111111111112,411194012689530881,'ea3184c0-0c70-4ab5-af24-adb3ac3b6885',1111111111111111,'A665A45920422F9D417E4867EFDC4FB8A04A1F3FFF1FA07E998E86F7F7A27AE3','ACTIVE',0,NULL);

INSERT INTO tbl_principal_role (principal_role_id,role_id,principal_id,created_date,updated_date) VALUES
	 (2,1,1111111111111112,1758083048,1758083048);