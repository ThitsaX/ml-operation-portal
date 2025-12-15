package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.RemoveRoleFromPrincipalCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveRoleFromUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class RemoveRoleFromUserHandler
    extends OperationPortalUseCase<RemoveRoleFromUser.Input, RemoveRoleFromUser.Output>
    implements RemoveRoleFromUser {

    private final RemoveRoleFromPrincipalCommand removeRoleFromPrincipalCommand;

    public RemoveRoleFromUserHandler(PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     RemoveRoleFromPrincipalCommand removeRoleFromPrincipalCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.removeRoleFromPrincipalCommand = removeRoleFromPrincipalCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.removeRoleFromPrincipalCommand.execute(
            new RemoveRoleFromPrincipalCommand.Input(input.principalId(), input.roleId()));

        return new Output(output.removed());
    }

}
