package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.UpdateParticipantAmount;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class UpdateParticipantAmountHandler
    extends OperationPortalAuditableUseCase<UpdateParticipantAmount.Input, UpdateParticipantAmount.Output>
    implements UpdateParticipantAmount {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateParticipantAmountHandler.class);

    private final ParticipantHubClient participantHubClient;

    @Autowired
    public UpdateParticipantAmountHandler(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache,
                                          ActionAuthorizationManager actionAuthorizationManager,
                                          ParticipantHubClient participantHubClient) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.participantHubClient = participantHubClient;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        PostParticipantBalance.Request request = new PostParticipantBalance.Request(input.transferId(),
                                                                                    input.externalReference(),
                                                                                    input.action(),
                                                                                    input.reason(),
                                                                                    input.amount(),
                                                                                    input.extensionList());

        PostParticipantBalance.Response
            response =
            this.participantHubClient.postParticipantBalance(input.participantId(),
                                                             input.accountId(),
                                                             request);

        return new Output(response.accessKey(), response.secretKey());
    }

}
