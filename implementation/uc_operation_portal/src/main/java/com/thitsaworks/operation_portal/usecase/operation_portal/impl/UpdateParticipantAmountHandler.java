package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.HubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.PostParticipantBalance;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.UpdateParticipantAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.EnumSet;
import java.util.Set;

@Service
public class UpdateParticipantAmountHandler
    extends OperationPortalAuditableUseCase<UpdateParticipantAmount.Input, UpdateParticipantAmount.Output>
        implements UpdateParticipantAmount {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateParticipantAmountHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final HubClient hubClient;

    @Autowired
    public UpdateParticipantAmountHandler(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache,
                                          HubClient hubClient) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.hubClient = hubClient;
    }

    @Override
    public Output onExecute(Input input) throws DomainException, ConnectException {

        String participantId = "wallet1";

        String accountId = "6";

        PostParticipantBalance.Request request = new PostParticipantBalance.Request(input.transferId(),
                                                                                    input.externalReference(),
                                                                                    input.action(),
                                                                                    input.reason(),
                                                                                    input.amount());

        PostParticipantBalance.Response response = this.hubClient.postParticipantBalance(participantId,
                                                                                         accountId,
                                                                                         request);

        return new Output(response.accessKey(), response.secretKey());
    }

}
