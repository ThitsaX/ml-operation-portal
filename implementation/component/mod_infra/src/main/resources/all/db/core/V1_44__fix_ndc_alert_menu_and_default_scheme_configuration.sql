UPDATE tbl_menu
SET parent_id = '1',
    updated_date = UNIX_TIMESTAMP()
WHERE name = 'NDC Alert Settings'
  AND (parent_id IS NULL OR parent_id <> '1');

INSERT INTO tbl_menu (menu_id, name, parent_id, is_active, created_date, updated_date)
SELECT 33, 'NDC Alert Settings', '1', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()
WHERE NOT EXISTS (
    SELECT 1
    FROM tbl_menu
    WHERE name = 'NDC Alert Settings'
);

INSERT INTO tbl_menu (menu_id, name, parent_id, is_active, created_date, updated_date)
SELECT 32, 'Role Permissions', '28', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()
WHERE NOT EXISTS (
    SELECT 1
    FROM tbl_menu
    WHERE name = 'Role Permissions'
);

INSERT INTO tbl_threshold_configuration (
    id,
    scope_type,
    dfsp_id,
    threshold_enabled,
    status,
    created_by,
    updated_by,
    created_date,
    updated_date
)
SELECT
    1111111111111115,
    'SCHEME',
    NULL,
    0,
    'ACTIVE',
    'system',
    NULL,
    UNIX_TIMESTAMP(),
    NULL
WHERE NOT EXISTS (
    SELECT 1
    FROM tbl_threshold_configuration
    WHERE scope_type = 'SCHEME'
);
