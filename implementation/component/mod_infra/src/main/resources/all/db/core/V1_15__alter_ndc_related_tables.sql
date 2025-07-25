ALTER TABLE tbl_participant_ndc
DROP COLUMN ndc_amount;

ALTER TABLE tbl_participant_ndc_history
DROP COLUMN ndc_amount;

ALTER TABLE tbl_participant_ndc
MODIFY COLUMN ndc_percent DECIMAL(7,4);

ALTER TABLE tbl_participant_ndc_history
MODIFY COLUMN ndc_percent DECIMAL(7,4);

