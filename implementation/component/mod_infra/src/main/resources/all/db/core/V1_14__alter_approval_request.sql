ALTER TABLE tbl_approval_request
ADD COLUMN participant_position_currency_id VARCHAR(100) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL AFTER `participant_settlement_currency_id`,
CHANGE COLUMN `participant_currency_id` `participant_settlement_currency_id` VARCHAR(100) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL ;
