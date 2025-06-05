-- dfsp_portal.tbl_announcement definition

CREATE TABLE `tbl_announcement` (
  `announcement_id` bigint NOT NULL,
  `announcement` text,
  `announcement_date` bigint DEFAULT NULL,
  `created_date` bigint DEFAULT NULL,
  `updated_date` bigint DEFAULT NULL,
  PRIMARY KEY (`announcement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE dfsp_portal.tbl_announcement CHANGE announcement announcement_title TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE dfsp_portal.tbl_announcement MODIFY COLUMN announcement_title TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE dfsp_portal.tbl_announcement ADD announcement_detail TEXT NULL;
ALTER TABLE dfsp_portal.tbl_announcement ADD is_deleted BIT NULL;

