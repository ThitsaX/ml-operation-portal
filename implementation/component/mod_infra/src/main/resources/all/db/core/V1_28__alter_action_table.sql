ALTER TABLE tbl_action ADD category varchar(255) NULL;
ALTER TABLE tbl_action CHANGE category category varchar(255) NULL AFTER `scope`;
ALTER TABLE tbl_action CHANGE `scope` `scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL AFTER action_code;
ALTER TABLE tbl_action CHANGE description description varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER category;
ALTER TABLE tbl_action ADD is_mandatory TINYINT(1) DEFAULT false NOT NULL;
ALTER TABLE tbl_action CHANGE is_mandatory is_mandatory TINYINT(1) DEFAULT false NOT NULL AFTER `category`;
