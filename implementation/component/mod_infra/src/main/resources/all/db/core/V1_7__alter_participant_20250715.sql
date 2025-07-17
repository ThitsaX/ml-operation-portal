ALTER TABLE tbl_participant ADD logo_data_type varchar(225) NULL;
ALTER TABLE tbl_participant CHANGE logo_data_type logo_data_type varchar(225) NULL AFTER mobile;
ALTER TABLE tbl_participant ADD logo BLOB NULL;
ALTER TABLE tbl_participant CHANGE logo logo BLOB NULL AFTER logo_data_type;
