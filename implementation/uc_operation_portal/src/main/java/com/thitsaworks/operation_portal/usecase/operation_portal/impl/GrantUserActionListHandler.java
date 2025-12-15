package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.GrantPrincipalActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantUserActionList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GrantUserActionListHandler
    extends OperationPortalUseCase<GrantUserActionList.Input, GrantUserActionList.Output>
    implements GrantUserActionList {

    private final GrantPrincipalActionCommand grantPrincipalActionCommand;

    public GrantUserActionListHandler(PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      GrantPrincipalActionCommand grantPrincipalActionCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.grantPrincipalActionCommand = grantPrincipalActionCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        for (var actionId : input.actionIdList()) {

            this.grantPrincipalActionCommand.execute(new GrantPrincipalActionCommand.Input(input.principalId(),
                                                                                           actionId));
        }

        return new Output(true);

    }

}