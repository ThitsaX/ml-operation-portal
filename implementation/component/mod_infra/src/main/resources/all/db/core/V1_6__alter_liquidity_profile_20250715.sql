ALTER TABLE tbl_liquidity_profile ADD bank_name varchar(200) NULL;
ALTER TABLE tbl_liquidity_profile CHANGE bank_name bank_name varchar(200) NULL AFTER participant_id;
