package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.query.RoleQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetRoleListByParticipant;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRoleListByParticipantHandler
    extends OperationPortalUseCase<GetRoleListByParticipant.Input, GetRoleListByParticipant.Output>
    implements GetRoleListByParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(GetRoleListByParticipantHandler.class);

    private final RoleQuery roleQuery;

    private final UserPermissionManager userPermissionManager;

    public GetRoleListByParticipantHandler(PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           RoleQuery roleQuery,
                                           UserPermissionManager userPermissionManager) {

        super(principalCache,
              actionAuthorizationManager);

        this.roleQuery = roleQuery;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var currentUser = this.userPermissionManager.getCurrentUser();

        List<RoleData> roleList = this.roleQuery.getAll();

        boolean isDfspUser = this.userPermissionManager.isDfsp(currentUser.principalId());

        if (isDfspUser || !input.participantName()
                                .equalsIgnoreCase("hub")) {
            roleList = roleList.stream()
                               .filter(role -> role.name() != null && role.name()
                                                                          .startsWith("DFSP"))
                               .toList();
        }

        return new Output(roleList);
    }

}
