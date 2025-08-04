-- Add default system roles
INSERT INTO tbl_role (role_id, name, active, created_date, updated_date) VALUES
    (1, 'HUB-Admin', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (2, 'HUB-Manager', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (3, 'HUB-Operator', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (4, 'HUB-User', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (5, 'DFSP-Admin', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
    (6, 'DFSP-Operation', 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000)
AS new_roles
ON DUPLICATE KEY UPDATE
    name = new_roles.name,
    active = new_roles.active,
    updated_date = UNIX_TIMESTAMP() * 1000;

-- Add grantRoleActions action
INSERT INTO iam_action (action_id, action_code, scope, description, created_date, updated_date) VALUES
    (1, 'GrantRoleActions', 'OPERATION_PORTAL', 'Auto-registered action for use case: GrantRoleActions', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000)
AS new_actions
ON DUPLICATE KEY UPDATE
    action_code = new_actions.action_code,
    scope = new_actions.scope,
    description = new_actions.description,
    updated_date = UNIX_TIMESTAMP() * 1000;

-- Grant grantRoleActions to HUB-Admin role (role_id = 1)
INSERT INTO tbl_role_grant (grant_id, role_id, action_id, created_date, updated_date) VALUES
    (1, 1, 1, UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000)
AS new_grants
ON DUPLICATE KEY UPDATE
    role_id = new_grants.role_id,
    action_id = new_grants.action_id,
    updated_date = UNIX_TIMESTAMP() * 1000;