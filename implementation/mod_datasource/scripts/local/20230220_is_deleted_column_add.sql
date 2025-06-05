ALTER TABLE dfsp_portal.tbl_participant_user ADD is_deleted BIT NULL;
ALTER TABLE dfsp_portal.tbl_hub_user ADD is_deleted BIT NULL;

UPDATE tbl_participant_user SET is_deleted = 0;
UPDATE tbl_hub_user SET is_deleted = 0;