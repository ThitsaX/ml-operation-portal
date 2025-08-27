ALTER TABLE tbl_role
ADD COLUMN is_dfsp TINYINT(1) DEFAULT 1 AFTER active;

INSERT INTO tbl_role (role_id, name, active, is_dfsp, created_date, updated_date) VALUES
    (1, 'HUB-Admin', 1, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
    (2, 'HUB-Manager', 1, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
    (3, 'HUB-Operator', 1, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
    (4, 'HUB-User', 1, 0, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
    (5, 'DFSP-Admin', 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()),
    (6, 'DFSP-Operation', 1, 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP())
AS new_roles
ON DUPLICATE KEY UPDATE
    name = new_roles.name,
    active = new_roles.active,
    is_dfsp = new_roles.is_dfsp,
    updated_date = UNIX_TIMESTAMP();
