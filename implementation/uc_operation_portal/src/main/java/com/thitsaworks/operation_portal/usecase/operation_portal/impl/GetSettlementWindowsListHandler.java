package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindowsList;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

@Service
public class GetSettlementWindowsListHandler
    extends OperationPortalAuditableUseCase<com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindowsList.Input, com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindowsList.Output>
        implements com.thitsaworks.operation_portal.usecase.operation_portal.GetSettlementWindowsList {

    private static final Logger logger = LoggerFactory.getLogger(GetSettlementWindowsListHandler.class);

    private final SettlementHubClient settlementHubClient;

    @Autowired
    public GetSettlementWindowsListHandler(CreateInputAuditCommand createInputAuditCommand,
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

        GetSettlementWindowsList.Request request = new GetSettlementWindowsList.Request();

        List<GetSettlementWindowsList.SettlementWindow> response =
                this.settlementHubClient.getSettlementWindowsList(
                        input.fromDate(),
                        input.toDate(),
                        input.currency(),
                        input.state(),
                        input.participantId(),
                        request
                                                                     );

        return new Output(response);
    }}