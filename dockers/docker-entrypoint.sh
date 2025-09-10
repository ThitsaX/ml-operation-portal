#!/bin/sh
# docker-entrypoint.sh

# run the main container command
java "-DVAULT_ADDRESS=${VAULT_ADDRESS}" "-DVAULT_TOKEN=${VAULT_TOKEN}" "-DJASYPT_PASSWORD=${JASYPT_PASSWORD}" -cp hub_operator/hub_operator_api.jar:hub_operator/lib/* com.thitsaworks.dfsp_portal.api.hub_operator.WebApiHubOperatorApplication
## Wait for any process to exit
#wait -n
## Exit with status of process that exited first
#exit $?