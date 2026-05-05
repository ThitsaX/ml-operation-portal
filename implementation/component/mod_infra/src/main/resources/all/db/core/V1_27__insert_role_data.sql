INSERT INTO tbl_role (role_id,name,active,is_dfsp,created_date,updated_date) VALUES
(7,'SYSTEM-Admin',1,0,1777957975,1777957975);

INSERT INTO tbl_role_grant (grant_id,role_id,action_id,created_date,updated_date) VALUES
    (2,7,1,1777957975,1777957975);