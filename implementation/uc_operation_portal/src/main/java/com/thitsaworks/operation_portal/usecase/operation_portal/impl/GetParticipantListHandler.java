package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;

import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class GetParticipantListHandler
    extends OperationPortalAuditableUseCase<GetParticipantList.Input, GetParticipantList.Output>
    implements GetParticipantList {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantListHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final ParticipantQuery participantQuery;

    public GetParticipantListHandler(CreateInputAuditCommand createInputAuditCommand,
                                     CreateOutputAuditCommand createOutputAuditCommand,
                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                     ObjectMapper objectMapper,
                                     PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     ParticipantQuery participantQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.participantQuery = participantQuery;
    }

    @Override
    public GetParticipantList.Output onExecute(GetParticipantList.Input input) throws DomainException {

        List<ParticipantData> participantDataList = this.participantQuery.getParticipants();

        List<GetParticipantList.Output.ParticipantInfo> participantInfoList = new ArrayList<>();

        for (ParticipantData participantData : participantDataList) {

            participantInfoList.add(
                new GetParticipantList.Output.ParticipantInfo(participantData.participantId(),
                                                              participantData.participantName()
                                                                            .getValue(),
                                                              participantData.description(),
                                                              participantData.address(),
                                                              participantData.mobile(),
                                                              participantData.logoDataType(),
                                                              participantData.logo(),
                                                              Instant.ofEpochSecond(participantData.createdDate())));
        }

        return new GetParticipantList.Output(participantInfoList);
    }

}
