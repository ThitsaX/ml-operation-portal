package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionListByUser;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

@Service
public class GetActionListByUserHandler
    extends OperationPortalUseCase<GetActionListByUser.Input, GetActionListByUser.Output>
    implements GetActionListByUser {

    private final IAMQuery iamQuery;

    private final PrincipalCache principalCache;

    private final UserPermissionManager userPermissionManager;

    public GetActionListByUserHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      IAMQuery iamQuery,
                                      UserPermissionManager userPermissionManager) {

        super(principalCache,
              actionAuthorizationManager);

        this.iamQuery = iamQuery;
        this.principalCache = principalCache;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var currentUser = this.userPermissionManager.getCurrentUser();

        var output = this.iamQuery.getGrantedActionsByPrincipal(currentUser.principalId())
                                  .stream()
                                  .map(action -> new Output.Action(action.actionId(),
                                                                   action.actionCode()
                                                                         .getValue()))
                                  .toList();

        return new Output(output);

    }

}