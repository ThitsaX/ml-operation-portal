package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetUser;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetUserHandler
    extends OperationPortalUseCase<GetUser.Input, GetUser.Output>
    implements GetUser {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserHandler.class);

    private final UserQuery userQuery;

    private final IAMQuery iamQuery;

    private final PrincipalCache principalCache;

    private final UserPermissionManager userPermissionManager;

    @Autowired
    public GetUserHandler(PrincipalCache principalCache,
                          ActionAuthorizationManager actionAuthorizationManager,
                          UserQuery userQuery,
                          IAMQuery iamQuery,
                          UserPermissionManager userPermissionManager) {

        super(principalCache,
              actionAuthorizationManager);

        this.userQuery = userQuery;
        this.iamQuery = iamQuery;
        this.principalCache = principalCache;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        var userId = input.userId();
        var participantId = new ParticipantId(this.principalCache.get(new PrincipalId(userId.getEntityId()))
                                                                 .realmId()
                                                                 .getId());



        var requestingPrincipleId = new PrincipalId(securityContext.userId());
        var requestingParticipantId = new ParticipantId(this.principalCache.get(requestingPrincipleId)
                                                                           .realmId()
                                                                           .getId());

        if (this.userPermissionManager.isDfsp(requestingPrincipleId)) {

            if (!participantId.equals(requestingParticipantId)) {
                throw new IAMException(IAMErrors.UNAUTHORIZED_USER_ACCESS);
            }
        }

        UserData userData = this.userQuery.get(userId);

        var
            roleList =
            this.iamQuery.getRolesByPrincipal(new PrincipalId(userId.getId()))
                         .stream()
                         .map(RoleData::name)
                         .toList();

        return new GetUser.Output(new UserId(userData.userId()
                                                     .getId()),
                                  userData.name(),
                                  userData.email(),
                                  userData.firstName(),
                                  userData.lastName(),
                                  userData.jobTitle(),
                                  roleList,
                                  userData.participantId(),
                                  userData.createdDate());
    }

}
