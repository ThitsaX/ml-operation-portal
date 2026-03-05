package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.CreateRoleCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateRole;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class CreateRoleHandler extends OperationPortalUseCase<CreateRole.Input, CreateRole.Output>
    implements CreateRole {

    private static final Logger LOG = LoggerFactory.getLogger(CreateRoleHandler.class);

    private final CreateRoleCommand createRoleCommand;

    public CreateRoleHandler(PrincipalCache principalCache,
                             ActionAuthorizationManager actionAuthorizationManager,
                             CreateRoleCommand createRoleCommand) {

        super(principalCache, actionAuthorizationManager);

        this.createRoleCommand = createRoleCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.createRoleCommand.execute(new CreateRoleCommand.Input(input.name(), input.isDfsp()));

        return new Output(output.roleId());
    }

}
