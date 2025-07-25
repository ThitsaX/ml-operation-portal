#!/bin/sh
# docker-entrypoint.sh

# run the main container command
java "-DVAULT_ADDRESS=${VAULT_ADDRESS}" "-DVAULT_TOKEN=${VAULT_TOKEN}" "-DENGINE_PATH=${ENGINE_PATH}" "-DCENTRAL_LEDGER_ENDPOINT=${CENTRAL_LEDGER_ENDPOINT}" -cp operation_api.jar:lib/* com.thitsaworks.operation_portal.api.operation.portal.WebApiOperationPortalApplication
## Wait for any process to exit
#wait -n
## Exit with status of process that exited first
#exit $?