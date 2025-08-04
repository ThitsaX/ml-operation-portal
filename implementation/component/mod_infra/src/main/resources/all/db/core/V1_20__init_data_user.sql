INSERT INTO tbl_user (user_id, access_key, secret_key, realm, realm_id, sha_256_password_hex, status, created_date, updated_date) VALUES (
    1111111111111111, 
    411194012689530880, 
    'ea3184c0-0c70-4ab5-af24-adb3ac3b6885', 
    'HUB_OPERATOR',
    NULL,
    '8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92', 
    'ACTIVE',  
    UNIX_TIMESTAMP() * 1000, 
    UNIX_TIMESTAMP() * 1000
) ON DUPLICATE KEY UPDATE
    access_key = VALUES(access_key),
    secret_key = VALUES(secret_key),
    realm = VALUES(realm),
    realm_id = VALUES(realm_id),
    sha_256_password_hex = VALUES(sha_256_password_hex),
    status = VALUES(status),
    updated_date = VALUES(updated_date);

-- Assign HUB-Admin role to the user
INSERT INTO tbl_user_role (user_role_id, user_id, role_id, created_date, updated_date) VALUES (
    1,
    1111111111111111,
    1,
    UNIX_TIMESTAMP() * 1000,
    UNIX_TIMESTAMP() * 1000
) ON DUPLICATE KEY UPDATE 
    role_id = VALUES(role_id),
    updated_date = VALUES(updated_date);
