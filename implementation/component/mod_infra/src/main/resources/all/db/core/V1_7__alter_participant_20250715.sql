ALTER TABLE tbl_participant ADD logo BLOB NULL;
ALTER TABLE tbl_participant CHANGE logo logo BLOB NULL AFTER mobile;

