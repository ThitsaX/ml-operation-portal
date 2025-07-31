-- Add default system roles
INSERT INTO tbl_role (role_id, name, active, created_date, updated_date) VALUES
    (1, 'HUB-Admin', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (2, 'HUB-Manager', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (3, 'HUB-Operator', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (4, 'HUB-User', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (5, 'DFSP-Admin', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (6, 'DFSP-Operation', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE 
    name = VALUES(name),
    active = VALUES(active),
    updated_date = UNIX_TIMESTAMP() * 1000;
