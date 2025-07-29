#!/bin/bash

# Enable the KV secrets engine if not already enabled
echo "Enabling KV secrets engine (version 2) at path 'operation_portal'..."
vault secrets enable -path=operation_portal -version=2 kv || echo "KV secrets engine already enabled at path 'operation_portal'."


# Redis Settings
REDIS_SETTINGS_PATH="operation_portal/redis/settings"
REDIS_SETTINGS_DATA='{
  "redisUrl": "redis://redis:6379"
}'

# Portal Data Settings
MYSQL_PORTAL_DATA_FLYWAY_SETTINGS_PATH="operation_portal/mysql/portal_data/flyway/settings"
MYSQL_PORTAL_DATA_FLYWAY_SETTINGS_DATA='{
   "locations": [
     "classpath:db/core"
   ],
   "url": "jdbc:mysql://mysql-operation-portal:3306/operation_portal?createDatabaseIfNotExist=true",
   "username": "root",
   "password": "admin"
}'

MYSQL_PORTAL_DATA_WRITE_SETTINGS_PATH="operation_portal/mysql/portal_data/write_db/settings"
MYSQL_PORTAL_DATA_WRITE_SETTINGS_DATA='{
   "url": "jdbc:mysql://mysql-operation-portal:3306/operation_portal",
   "username": "operation_portal_jdbc",
   "password": "password",
   "minPoolSize": 10,
   "maxPoolSize": 20
}'

MYSQL_PORTAL_DATA_READ_SETTINGS_PATH="operation_portal/mysql/portal_data/read_db/settings"
MYSQL_PORTAL_DATA_READ_SETTINGS_DATA='{
   "url": "jdbc:mysql://mysql-operation-portal:3306/operation_portal",
   "username": "operation_portal_ro",
   "password": "password",
   "minPoolSize": 10,
   "maxPoolSize": 20
}'

# Hub Data Settings
MYSQL_HUB_DATA_FLYWAY_SETTINGS_PATH="operation_portal/mysql/hub_data/flyway/settings"
MYSQL_HUB_DATA_FLYWAY_SETTINGS_DATA='{
   "locations": [
     "classpath:db/report"
   ],
   "url": "jdbc:mysql://mysql-central-ledger:3306/central_ledger?createDatabaseIfNotExist=true",
   "username": "root",
   "password": "admin"
}'

MYSQL_HUB_DATA_WRITE_SETTINGS_PATH="operation_portal/mysql/hub_data/write_db/settings"
MYSQL_HUB_DATA_WRITE_SETTINGS_DATA='{
   "url": "jdbc:mysql://mysql-central-ledger:3306/central_ledger",
   "username": "central_ledger_ro",
   "password": "password",
   "minPoolSize": 10,
   "maxPoolSize": 20
}'

MYSQL_HUB_DATA_READ_SETTINGS_PATH="operation_portal/mysql/hub_data/read_db/settings"
MYSQL_HUB_DATA_READ_SETTINGS_DATA='{
   "url": "jdbc:mysql://mysql-central-ledger:3306/central_ledger",
   "username": "central_ledger_ro",
   "password": "password",
   "minPoolSize": 10,
   "maxPoolSize": 20
}'

echo "Adding Redis Settings to Vault at path '$REDIS_SETTINGS_PATH'..."
vault kv put $REDIS_SETTINGS_PATH @<(echo "$REDIS_SETTINGS_DATA")

# Add Portal Secrets

echo "Adding Portal Data Flyway Settings to Vault at path '$MYSQL_PORTAL_DATA_FLYWAY_SETTINGS_PATH'..."
vault kv put $MYSQL_PORTAL_DATA_FLYWAY_SETTINGS_PATH @<(echo "$MYSQL_PORTAL_DATA_FLYWAY_SETTINGS_DATA")

echo "Adding Portal Data Write Data Source Settings to Vault at path '$MYSQL_PORTAL_DATA_WRITE_SETTINGS_PATH'..."
vault kv put $MYSQL_PORTAL_DATA_WRITE_SETTINGS_PATH @<(echo "$MYSQL_PORTAL_DATA_WRITE_SETTINGS_DATA")

echo "Adding Portal Data Read Data Source Settings to Vault at path '$MYSQL_PORTAL_DATA_READ_SETTINGS_PATH'..."
vault kv put $MYSQL_PORTAL_DATA_READ_SETTINGS_PATH @<(echo "$MYSQL_PORTAL_DATA_READ_SETTINGS_DATA")


# Add Hub Secrets

echo "Adding Hub Data Flyway Settings to Vault at path '$MYSQL_HUB_DATA_FLYWAY_SETTINGS_PATH'..."
vault kv put $MYSQL_HUB_DATA_FLYWAY_SETTINGS_PATH @<(echo "$MYSQL_HUB_DATA_FLYWAY_SETTINGS_DATA")

echo "Adding Hub Data Write Data Source Settings to Vault at path '$MYSQL_HUB_DATA_WRITE_SETTINGS_PATH'..."
vault kv put $MYSQL_HUB_DATA_WRITE_SETTINGS_PATH @<(echo "$MYSQL_HUB_DATA_WRITE_SETTINGS_DATA")

echo "Adding Hub Data Read Data Source Settings to Vault at path '$MYSQL_HUB_DATA_READ_SETTINGS_PATH'..."
vault kv put $MYSQL_HUB_DATA_READ_SETTINGS_PATH @<(echo "$MYSQL_HUB_DATA_READ_SETTINGS_DATA")




# Verify all secrets
echo "Verifying all secrets..."
vault kv get $REDIS_SETTINGS_PATH
vault kv get $MYSQL_PORTAL_DATA_FLYWAY_SETTINGS_PATH
vault kv get $MYSQL_PORTAL_DATA_WRITE_SETTINGS_PATH
vault kv get $MYSQL_PORTAL_DATA_READ_SETTINGS_PATH
vault kv get $MYSQL_HUB_DATA_FLYWAY_SETTINGS_PATH
vault kv get $MYSQL_HUB_DATA_WRITE_SETTINGS_PATH
vault kv get $MYSQL_HUB_DATA_READ_SETTINGS_PATH


echo "Vault initialization and secret creation complete."