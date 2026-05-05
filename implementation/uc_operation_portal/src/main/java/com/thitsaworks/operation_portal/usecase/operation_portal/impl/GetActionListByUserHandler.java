package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetActionListByUser;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetActionListByUserHandler
    extends OperationPortalUseCase<GetActionListByUser.Input, GetActionListByUser.Output>
    implements GetActionListByUser {

    private final IAMQuery iamQuery;

    private final UserPermissionManager userPermissionManager;

    private final ActionAuthorizationManager actionAuthorizationManager;

    public GetActionListByUserHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      IAMQuery iamQuery,
                                      UserPermissionManager userPermissionManager) {

        super(principalCache,
              actionAuthorizationManager);

        this.iamQuery = iamQuery;
        this.userPermissionManager = userPermissionManager;
        this.actionAuthorizationManager = actionAuthorizationManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var currentUser = this.userPermissionManager.getCurrentUser();

        var auditableActionNames = this.actionAuthorizationManager.findAuditableActions();

        List<Output.Action> actionList = this.iamQuery
                                             .getGrantedActionListByPrincipal(currentUser.principalId())
                                             .stream()
                                             .filter(action -> auditableActionNames.contains(action.actionCode()
                                                                                                   .getValue()))
                                             .map(action -> new Output.Action(
                                                 action.actionId(),
                                                 action.actionCode()
                                                       .getValue()
                                             ))
                                             .toList();

        return new Output(actionList);

    }

}