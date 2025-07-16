package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.GetParticipantProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
public class GetParticipantProfileHandler
    extends CoreServicesAuditableUseCase<GetParticipantProfile.Input, GetParticipantProfile.Output>
    implements GetParticipantProfile {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantProfileHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ParticipantQuery participantQuery;

    public GetParticipantProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        ParticipantQuery participantQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.participantQuery = participantQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var participantData = this.participantQuery.get(input.participantId());

        return new Output(participantData.participantId(),
                          participantData.dfspCode()
                                         .getValue(),
                          participantData.name(),
                          participantData.address(),
                          participantData.mobile(),
                          participantData.logoType(),
                          participantData.logo(),
                          Instant.ofEpochSecond(participantData.createdDate()));
    }

}
