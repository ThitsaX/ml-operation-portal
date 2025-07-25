CREATE TABLE iam_action (
    `action_id` BIGINT NOT NULL,
    `action_code` VARCHAR(255) NOT NULL,
    `scope` VARCHAR(255) NOT NULL,
    `description` VARCHAR(255),
    PRIMARY KEY (`action_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_blocked_action (
    `blocked_action_id` BIGINT NOT NULL,
    `user_id` BIGINT DEFAULT NULL,
    `action_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`blocked_action_id`),
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_user (
   `user_id` BIGINT NOT NULL,
   `access_key` bigint DEFAULT NULL,
   `secret_key` varchar(64) DEFAULT NULL,
   `realm` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
   `realm_id` bigint DEFAULT NULL,
   `sha_256_password_hex` varchar(64) DEFAULT NULL,
   `status` varchar(10) DEFAULT NULL,
   `created_date` bigint DEFAULT NULL,
   `updated_date` bigint DEFAULT NULL,
   `user_role_type` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`user_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_role (
    `role_id` BIGINT NOT NULL,
    `name` VARCHAR(255),
    `active` BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (`role_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_role_grant (
    `grant_id` BIGINT NOT NULL,
    `role_id` BIGINT DEFAULT NULL,
    `action_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`grant_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tbl_user_grant (
    `grant_id` BIGINT NOT NULL,
    `user_id` BIGINT DEFAULT NULL,
    `action_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (`grant_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE tbl_user_role (
    `user_role_id BIGINT NOT NULL,
    `role_id` BIGINT DEFAULT NULL,
    `user_id` BIGINT DEFAULT NULL,
    PRIMARY KEY (user_role_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


