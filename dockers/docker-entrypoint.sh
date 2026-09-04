#!/bin/sh
# docker-entrypoint.sh

# run the main container command
java \
  "-DVAULT_ADDRESS=${VAULT_ADDRESS}" \
  "-DVAULT_TOKEN=${VAULT_TOKEN}" \
  "-DENGINE_PATH=${ENGINE_PATH}" \
  "-DOPERATION_PORTAL_PORT_NO=${OPERATION_PORTAL_PORT_NO}" \
  "-DCENTRAL_LEDGER_ENDPOINT=${CENTRAL_LEDGER_ENDPOINT}" \
  "-DSETTLEMENT_ENDPOINT=${SETTLEMENT_ENDPOINT}" \
  "-DOPERATION_PORTAL_FRONTEND_ENDPOINT=${OPERATION_PORTAL_FRONTEND_ENDPOINT}" \
  "-DREPORT_PAGE_SIZE=${REPORT_PAGE_SIZE}" \
  "-DRECEIVER_BIC=${RECEIVER_BIC}" \
  "-DDISPUTE_URL=${DISPUTE_URL}" \
  "-DSERVICE_REQUEST_URL=${SERVICE_REQUEST_URL}" \
  "-DPACS_SENDER_GUIM_BIC=${PACS_SENDER_GUIM_BIC}" \
  "-DPACS_RECEIVER_BCRG_BIC=${PACS_RECEIVER_BCRG_BIC}" \
  "-DPACS_MEMBER_ID=${PACS_MEMBER_ID}" \
  -cp operation_api.jar:lib/* \
  com.thitsaworks.operation_portal.api.operation.portal.WebApiOperationPortalApplication
## Wait for any process to exit
#wait -n
## Exit with status of process that exited first
#exit $?
