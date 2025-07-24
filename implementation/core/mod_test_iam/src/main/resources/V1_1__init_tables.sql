CREATE TABLE iam_action (
    `action_id` BIGINT NOT NULL,
    `action_code` VARCHAR(255) NOT NULL,
    `scope` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255),
    PRIMARY KEY (action_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_blocked_action (
    `blocked_action_id` BIGINT NOT NULL,
    `iam_user_id` BIGINT DEFAULT NULL,
    `action_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (blocked_action_id),
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_user (
    `iamuser_id` BIGINT NOT NULL,
    `username` VARCHAR(255),
    `name` VARCHAR(255),
    `password_hash` VARCHAR(255),
    `sys_password_hash` VARCHAR(255),
    `access_key` VARCHAR(255),
    `secret_key` VARCHAR(255),
    `active` BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (iamuser_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_role (
    `role_id` BIGINT NOT NULL,
    `name` VARCHAR(255),
    `active` BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (role_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_role_grant (
    `grant_id` BIGINT NOT NULL,
    `role_id` BIGINT DEFAULT NULL,
    `action_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (grant_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_user_grant (
    `grant_id` BIGINT NOT NULL,
    `user_id` BIGINT DEFAULT NULL,
    `action_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (grant_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE tbl_user_role (
    `user_role_id BIGINT NOT NULL,
    `role_id` BIGINT DEFAULT NULL,
    `user_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (user_role_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


