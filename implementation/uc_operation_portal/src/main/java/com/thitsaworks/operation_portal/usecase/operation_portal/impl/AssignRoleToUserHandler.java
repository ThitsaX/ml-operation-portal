package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.AssignRoleToPrincipalCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.AssignRoleToUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class AssignRoleToUserHandler
    extends OperationPortalUseCase<AssignRoleToUser.Input, AssignRoleToUser.Output>
    implements AssignRoleToUser {

    private static final Logger LOG = LoggerFactory.getLogger(AssignRoleToUserHandler.class);

    private final AssignRoleToPrincipalCommand assignRoleToPrincipalCommand;

    public AssignRoleToUserHandler(PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   AssignRoleToPrincipalCommand assignRoleToPrincipalCommand) {

        super(principalCache,
              actionAuthorizationManager);

        this.assignRoleToPrincipalCommand = assignRoleToPrincipalCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var
            output =
            this.assignRoleToPrincipalCommand.execute(new AssignRoleToPrincipalCommand.Input(input.principalId(),
                                                                                             input.roleId()));

        return new Output(output.principalRoleId());
    }

}
