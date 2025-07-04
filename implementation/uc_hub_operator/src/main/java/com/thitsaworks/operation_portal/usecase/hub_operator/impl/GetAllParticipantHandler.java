package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.usecase.HubOperatorAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.hub_operator.GetAllParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetAllParticipantHandler
    extends HubOperatorAuditableUseCase<GetAllParticipant.Input, GetAllParticipant.Output>
    implements GetAllParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.SUPERUSER,
                                                                    UserRoleType.ADMIN,
                                                                    UserRoleType.REPORTING,
                                                                    UserRoleType.OPERATION);

    private final ParticipantQuery participantQuery;

    public GetAllParticipantHandler(CreateInputAuditCommand createInputAuditCommand,
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
    public GetAllParticipant.Output onExecute(GetAllParticipant.Input input) {

        List<ParticipantData> participantDataList = this.participantQuery.getParticipants();

        List<GetAllParticipant.Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (ParticipantData participantData : participantDataList) {

            participantInfoList.add(
                new GetAllParticipant.Output.ParticipantInfo(participantData.participantId(),
                                                             participantData.dfspCode().getValue(),
                                                             participantData.name(),
                                                             participantData.dfspName(),
                                                             participantData.address(),
                                                             participantData.mobile(),
                                                             Instant.ofEpochSecond(participantData.createdDate())));
        }

        return new GetAllParticipant.Output(participantInfoList);
    }

}
