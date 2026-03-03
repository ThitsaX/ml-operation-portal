ALTER TABLE tbl_participant_ndc
ADD COLUMN `ndc_amount` DECIMAL(18,4) NULL DEFAULT NULL AFTER `ndc_percent`,
ADD COLUMN `balance` DECIMAL(18,4) NULL DEFAULT NULL AFTER `ndc_amount`,
ADD COLUMN `made_by` VARCHAR(100) NULL DEFAULT NULL AFTER `balance`;

ALTER TABLE tbl_participant_ndc_history
ADD COLUMN `ndc_amount` DECIMAL(18,4) NULL DEFAULT NULL AFTER `ndc_percent`,
ADD COLUMN `balance` DECIMAL(18,4) NULL DEFAULT NULL AFTER `ndc_amount`,
ADD COLUMN `made_by` VARCHAR(100) NULL DEFAULT NULL AFTER `balance`;