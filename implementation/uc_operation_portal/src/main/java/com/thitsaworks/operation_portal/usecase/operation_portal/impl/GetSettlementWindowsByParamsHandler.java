package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindowsByParams;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class GetSettlementWindowsByParamsHandler
    extends OperationPortalAuditableUseCase<GetSettlementWindowsByParams.Input, GetSettlementWindowsByParams.Output>
        implements GetSettlementWindowsByParams {

    private static final Logger logger = LoggerFactory.getLogger(GetSettlementWindowsByParamsHandler.class);

    private final SettlementHubClient settlementHubClient;

    @Autowired
    public GetSettlementWindowsByParamsHandler(CreateInputAuditCommand createInputAuditCommand,
                                               CreateOutputAuditCommand createOutputAuditCommand,
                                               CreateExceptionAuditCommand createExceptionAuditCommand,
                                               ObjectMapper objectMapper,
                                               PrincipalCache principalCache,
                                               ActionAuthorizationManager actionAuthorizationManager,
                                               SettlementHubClient settlementHubClient) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);
        this.settlementHubClient = settlementHubClient;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindowsByParams.Request request = new com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindowsByParams.Request();

        List<com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindowsByParams.SettlementWindow> response =
                this.settlementHubClient.getSettlementWindowsByParams(
                        input.fromDate(),
                        input.toDate(),
                        input.currency(),
                        input.state(),
                        input.participantId(),
                        request
                                                                     );

        return new Output(response);
    }}