package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.GrantRoleActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantRoleActionList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GrantRoleActionListHandler
    extends OperationPortalUseCase<GrantRoleActionList.Input, GrantRoleActionList.Output>
    implements GrantRoleActionList {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionListHandler.class);

    private final GrantRoleActionCommand grantRoleActionCommand;

    public GrantRoleActionListHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      GrantRoleActionCommand grantRoleActionsCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.grantRoleActionCommand = grantRoleActionsCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        for (var singleRoleGrant : input.roleGrantList()) {

            for (var action : singleRoleGrant.actionList()) {
                this.grantRoleActionCommand.execute(new GrantRoleActionCommand.Input(singleRoleGrant.roleName(),
                                                                                     action));

            }
        }

        return new Output(true);
    }

}
