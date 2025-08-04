package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetExistingUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class GetExistingUserHandler
    extends OperationPortalAuditableUseCase<GetExistingUser.Input, GetExistingUser.Output>
    implements GetExistingUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final ParticipantUserQuery participantUserQuery;

    @Autowired
    public GetExistingUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                  CreateOutputAuditCommand createOutputAuditCommand,
                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                  ObjectMapper objectMapper,
                                  PrincipalCache principalCache,
                                  ActionAuthorizationManager actionAuthorizationManager,
                                  ParticipantUserQuery participantUserQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.participantUserQuery = participantUserQuery;
    }

    @Override
    public GetExistingUser.Output onExecute(GetExistingUser.Input input) throws DomainException {

        ParticipantUserData participantUserData = this.participantUserQuery.get(new ParticipantUserId(input.userId()
                                                                                                           .getId()));

        return new GetExistingUser.Output(new UserId(participantUserData.participantUserId().getId()),
                                          participantUserData.name(),
                                          participantUserData.email(),
                                          participantUserData.firstName(),
                                          participantUserData.lastName(),
                                          participantUserData.jobTitle(),
                                          participantUserData.participantId(),
                                          participantUserData.createdDate(),
                                          participantUserData.participantName().getValue());
    }

}
