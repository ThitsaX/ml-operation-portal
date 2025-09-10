#!/bin/sh
# docker-entrypoint.sh

# run the main container command
java \
  "-DVAULT_ADDRESS=${VAULT_ADDRESS}" \
  "-DVAULT_TOKEN=${VAULT_TOKEN}" \
  "-DENGINE_PATH=${ENGINE_PATH}" \
  "-DOPERATION_PORTAL_PORT_NO=${OPERATION_PORTAL_PORT_NO}" \
  "-DCENTRAL_LEDGER_END_POINT=${CENTRAL_LEDGER_ENDPOINT}" \
  "-DSETTLEMENT_END_POINT=${SETTLEMENT_END_POINT}" \
  -cp operation_api.jar:lib/* \
  com.thitsaworks.operation_portal.api.operation.portal.WebApiOperationPortalApplication
## Wait for any process to exit
#wait -n
## Exit with status of process that exited first
#exit $?