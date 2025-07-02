-- tbl_contact
ALTER TABLE operation_portal.tbl_contact ADD contact_type varchar(100) NOT NULL;
ALTER TABLE operation_portal.tbl_contact CHANGE contact_type contact_type varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL AFTER mobile;

-- tbl_participant
ALTER TABLE operation_portal.tbl_participant DROP COLUMN business_contact_id;
ALTER TABLE operation_portal.tbl_participant DROP COLUMN technical_contact_id;
