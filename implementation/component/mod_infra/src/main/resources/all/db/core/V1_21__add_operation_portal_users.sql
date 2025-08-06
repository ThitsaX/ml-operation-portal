CREATE USER IF NOT EXISTS 'operation_portal_jdbc'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'operation_portal_jdbc'@'%';
CREATE USER IF NOT EXISTS 'operation_portal_ro'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *.* TO 'operation_portal_ro'@'%';
