ALTER TABLE tbl_settlement_model ADD manual_close_window BIT NOT NULL AFTER auto_close_window;
ALTER TABLE tbl_settlement_model ADD zone_id varchar(50) NULL AFTER manual_close_window;

