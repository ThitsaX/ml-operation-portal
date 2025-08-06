package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserHandler
    extends OperationPortalAuditableUseCase<GetUser.Input, GetUser.Output>
        implements GetUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserHandler.class);

    private final ParticipantUserQuery participantUserQuery;

    @Autowired
    public GetUserHandler(CreateInputAuditCommand createInputAuditCommand,
                          CreateOutputAuditCommand createOutputAuditCommand,
                          CreateExceptionAuditCommand createExceptionAuditCommand,
                          ObjectMapper objectMapper,
                          PrincipalCache principalCache,
                          ActionAuthorizationManager actionAuthorizationManager,
                          ParticipantUserQuery participantUserQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.participantUserQuery = participantUserQuery;
    }

    @Override
    public GetUser.Output onExecute(GetUser.Input input) throws DomainException {

        ParticipantUserData participantUserData = this.participantUserQuery.get(new ParticipantUserId(input.userId()
                                                                                                           .getId()));

        return new GetUser.Output(new UserId(participantUserData.participantUserId()
                                                                .getId()),
                                  participantUserData.name(),
                                  participantUserData.email(),
                                  participantUserData.firstName(),
                                  participantUserData.lastName(),
                                  participantUserData.jobTitle(),
                                  participantUserData.participantId(),
                                  participantUserData.createdDate(),
                                  participantUserData.participantName()
                                                             .getValue());
    }

}
