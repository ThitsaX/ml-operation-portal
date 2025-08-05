
-- Assign HUB-Admin role to the user
INSERT INTO tbl_principal_role (principal_role_id, principal_id, role_id, created_date, updated_date) VALUES (
    1,
    1111111111111111,
    1,
    UNIX_TIMESTAMP() * 1000,
    UNIX_TIMESTAMP() * 1000
) AS new_user_role
ON DUPLICATE KEY UPDATE
    role_id = new_user_role.role_id,
    updated_date = new_user_role.updated_date;