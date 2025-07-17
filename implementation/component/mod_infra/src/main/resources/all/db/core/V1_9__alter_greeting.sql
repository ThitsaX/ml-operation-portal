-- tbl_greeting
ALTER TABLE tbl_greeting
ADD COLUMN is_deleted BIT(1) DEFAULT NULL,
ADD COLUMN greeting_date BIGINT DEFAULT NULL;







