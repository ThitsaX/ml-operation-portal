INSERT INTO tbl_menu (menu_id, name, parent_id, is_active, created_date, updated_date)
SELECT 35, 'Pacs 029 Transaction Settlement Report', '13', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()
WHERE NOT EXISTS (
    SELECT 1 FROM tbl_menu WHERE name = 'Pacs 029 Transaction Settlement Report'
);

INSERT INTO tbl_menu (menu_id, name, parent_id, is_active, created_date, updated_date)
SELECT 36, 'Pacs 029 Fee Settlement Report', '13', 1, UNIX_TIMESTAMP(), UNIX_TIMESTAMP()
WHERE NOT EXISTS (
    SELECT 1 FROM tbl_menu WHERE name = 'Pacs 029 Fee Settlement Report'
);
