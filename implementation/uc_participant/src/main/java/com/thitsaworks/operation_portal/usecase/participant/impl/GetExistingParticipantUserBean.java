package com.thitsaworks.operation_portal.usecase.participant.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.ParticipantAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.participant.GetExistingParticipantUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GetExistingParticipantUserBean extends ParticipantAuditableUseCase<GetExistingParticipantUser.Input,GetExistingParticipantUser.Output> implements GetExistingParticipantUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantUserBean.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final ParticipantUserQuery participantUserQuery;

    public GetExistingParticipantUserBean(CreateInputAuditCommand createInputAuditCommand,
                                          CreateOutputAuditCommand createOutputAuditCommand,
                                          CreateExceptionAuditCommand createExceptionAuditCommand,
                                          ObjectMapper objectMapper,
                                          PrincipalCache principalCache,
                                          ParticipantUserQuery participantUserQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);
        this.participantUserQuery = participantUserQuery;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ParticipantUserData participantUserData = this.participantUserQuery.get(input.participantUserId());

        return new Output(participantUserData.participantUserId(),
                          participantUserData.name(),
                          participantUserData.email(),
                          participantUserData.firstName(),
                          participantUserData.lastName(),
                          participantUserData.jobTitle(),
                          participantUserData.participantId(),
                          participantUserData.createdDate(),
                          participantUserData.dfspCode().getValue());

    }
}
