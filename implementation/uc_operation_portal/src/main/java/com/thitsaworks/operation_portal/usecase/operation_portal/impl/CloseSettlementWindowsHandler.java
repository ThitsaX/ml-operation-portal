package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CloseSettlementWindows;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class CloseSettlementWindowsHandler
    extends OperationPortalAuditableUseCase<CloseSettlementWindows.Input, CloseSettlementWindows.Output>
    implements CloseSettlementWindows {

    private static final Logger LOG = LoggerFactory.getLogger(CloseSettlementWindowsHandler.class);

    private final SettlementHubClient settlementHubClient;

    @Autowired
    public CloseSettlementWindowsHandler(CreateInputAuditCommand createInputAuditCommand,
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
    public Output onExecute(Input input) throws DomainException, ConnectException {

        PostCloseSettlementWindows.Request request = new PostCloseSettlementWindows.Request(
            input.state(),
            input.reason());

        LOG.info("Close Settlement Windows Request from op to mojaloop system : {}", request);

        PostCloseSettlementWindows.Response
            response =
            this.settlementHubClient.closeSettlementWindows(input.settlementWindowId(), request);

        LOG.info("Close Settlement Windows Response from mojaloop to op system : {}", response);


        return new Output(input.settlementWindowId(),
                          request.state(),
                          request.reason(),
                          response.createdDate(),
                          response.closedDate(),
                          response.changedDate());
    }

}
