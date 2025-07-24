ALTER TABLE `operation_portal`.`tbl_approval_request`
CHANGE COLUMN `dfsp` `participant_name` VARCHAR(100) NOT NULL,
CHANGE COLUMN `currency` `participant_currency` VARCHAR(32) NOT NULL AFTER `participant_name`,
ADD COLUMN `participant_currency_id` VARCHAR(100) NOT NULL AFTER `participant_currency`;

