DROP TABLE operation_portal.tbl_hub_user;

INSERT INTO tbl_participant (participant_id,participant_name,description,address,mobile,logo_data_type,logo,created_date,updated_date)
	VALUES (1111111111111111,'hub','Hub Operator',NULL,NULL,NULL,0x5B4E554C4C5D,UNIX_TIMESTAMP(),UNIX_TIMESTAMP());

UPDATE tbl_principal SET realm_id=1111111111111111 WHERE principal_id=1111111111111111;

INSERT INTO tbl_participant_user (user_id, participant_id, name,email,first_name,last_name,job_title,created_date,updated_date,is_deleted)
	VALUES (1111111111111111, 1111111111111111, 'thitsaworks','hub.user@thitsaworks.com','Thitsaworks','User','Hub Operator',UNIX_TIMESTAMP(),UNIX_TIMESTAMP(),0);