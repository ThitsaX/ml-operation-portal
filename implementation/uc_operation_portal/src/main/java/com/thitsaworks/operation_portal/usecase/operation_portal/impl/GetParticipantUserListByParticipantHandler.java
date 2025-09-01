package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.PrincipalRoleQuery;
import com.thitsaworks.operation_portal.core.iam.query.RoleQuery;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetParticipantUserListByParticipant;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GetParticipantUserListByParticipantHandler extends OperationPortalAuditableUseCase<GetParticipantUserListByParticipant.Input, GetParticipantUserListByParticipant.Output>
    implements GetParticipantUserListByParticipant {

    private final UserQuery userQuery;

    private final PrincipalCache principalCache;

    private final PrincipalRoleQuery principalRoleQuery;

    private final RoleQuery roleQuery;

    public GetParticipantUserListByParticipantHandler(CreateInputAuditCommand createInputAuditCommand,
                                                      CreateOutputAuditCommand createOutputAuditCommand,
                                                      CreateExceptionAuditCommand createExceptionAuditCommand,
                                                      ObjectMapper objectMapper,
                                                      PrincipalCache principalCache,
                                                      ActionAuthorizationManager actionAuthorizationManager,
                                                      UserQuery userQuery,
                                                      PrincipalCache principalCache1,
                                                      PrincipalRoleQuery principalRoleQuery,
                                                      RoleQuery roleQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.userQuery = userQuery;
        this.principalCache = principalCache1;
        this.principalRoleQuery = principalRoleQuery;
        this.roleQuery = roleQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData principalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (principalData == null) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);

        }

        Set<Output.User> madeByUsers = new HashSet<>();
        var principalRole = this.principalRoleQuery.getRole(principalData.principalId());

        var role = this.roleQuery.get(principalRole.roleId());

        if (role.isDfsp()) {
            var userDataList = this.userQuery.getUsers(new ParticipantId(principalData.realmId()
                                                                                      .getId()));

            for (var userData : userDataList) {

                madeByUsers.add(new Output.User(new UserId(userData.userId()
                                                                   .getEntityId()), userData.email()));

            }

            return new Output(madeByUsers);
        } else {

            List<UserData> userDataList = this.userQuery.getUsers();

            for (var userData : userDataList) {

                madeByUsers.add(new Output.User(new UserId(userData.userId()
                                                                   .getEntityId()), userData.email()));
            }

            return new Output(madeByUsers);

        }

    }

}