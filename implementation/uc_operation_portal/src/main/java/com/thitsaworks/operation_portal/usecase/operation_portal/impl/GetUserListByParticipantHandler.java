package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetUserListByParticipant;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetUserListByParticipantHandler
    extends OperationPortalAuditableUseCase<GetUserListByParticipant.Input, GetUserListByParticipant.Output>
    implements GetUserListByParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserListByParticipantHandler.class);

    private final UserQuery userQuery;

    private final PrincipalCache principalCache;

    private final IAMQuery iamQuery;

    private final UserPermissionManager userPermissionManager;

    public GetUserListByParticipantHandler(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           UserQuery userQuery,
                                           IAMQuery iamQuery,
                                           UserPermissionManager userPermissionManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.userQuery = userQuery;
        this.principalCache = principalCache;
        this.iamQuery = iamQuery;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var principalId = new PrincipalId(input.userId()
                                               .getId());
        var participantId = new ParticipantId(this.principalCache.get(principalId)
                                                                 .realmId()
                                                                 .getId());

        List<UserData> userDataList;
        if (this.userPermissionManager.isDfsp(principalId)) {

            userDataList = this.userQuery.getUsers(participantId);

        } else {
            userDataList = this.userQuery.getUsers();
        }

        List<GetUserListByParticipant.UserInfo> userInfoList = new ArrayList<>();

        for (UserData userData : userDataList) {

            PrincipalData
                principalData =
                this.principalCache.get(new PrincipalId(userData.userId()
                                                                .getId()));

            var
                roleList =
                this.iamQuery.getRolesByPrincipal(principalData.principalId())
                             .stream()
                             .map(RoleData::name)
                             .toList();

            userInfoList.add(new GetUserListByParticipant.UserInfo(userData.userId(),
                                                                   userData.name(),
                                                                   userData.email(),
                                                                   userData.firstName(),
                                                                   userData.lastName(),
                                                                   userData.jobTitle(),
                                                                   roleList,
                                                                   principalData.principalStatus()
                                                                   .toString(),
                                                                   Instant.ofEpochSecond(userData.createdDate())));
        }

        return new Output(userInfoList);
    }

}
