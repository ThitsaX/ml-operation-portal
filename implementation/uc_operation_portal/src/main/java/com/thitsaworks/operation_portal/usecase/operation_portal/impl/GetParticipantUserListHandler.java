package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantUserList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetParticipantUserListHandler
    extends OperationPortalAuditableUseCase<GetParticipantUserList.Input, GetParticipantUserList.Output>
    implements GetParticipantUserList {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantUserListHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final ParticipantUserQuery participantUserQuery;

    private final PrincipalCache principalCache;

    public GetParticipantUserListHandler(CreateInputAuditCommand createInputAuditCommand,
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
        this.principalCache = principalCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        List<ParticipantUserData> participantUserDataList =
            this.participantUserQuery.getParticipantUsers(null);

        List<GetParticipantUserList.UserInfo> userInfoList = new ArrayList<>();

        for (ParticipantUserData participantUserData : participantUserDataList) {

            PrincipalData
                principalData =
                this.principalCache.get(new PrincipalId(participantUserData.participantUserId()
                                                                           .getId()));

            userInfoList.add(new GetParticipantUserList.UserInfo(participantUserData.participantUserId(),
                                                                 participantUserData.name(),
                                                                 participantUserData.email(),
                                                                 participantUserData.firstName(),
                                                                 participantUserData.lastName(),
                                                                 participantUserData.jobTitle(),
                                                                 null,
                                                                 principalData.principalStatus()
                                                                             .toString(),
                                                                 Instant.ofEpochSecond(participantUserData.createdDate())));
        }

        return new Output(null);
    }

}
