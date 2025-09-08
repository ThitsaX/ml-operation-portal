package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.RemoveRoleFromPrincipalCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveRoleFromUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class RemoveRoleFromUserHandler
    extends OperationPortalAuditableUseCase<RemoveRoleFromUser.Input, RemoveRoleFromUser.Output>
    implements RemoveRoleFromUser {

    private final RemoveRoleFromPrincipalCommand removeRoleFromPrincipalCommand;

    public RemoveRoleFromUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                     CreateOutputAuditCommand createOutputAuditCommand,
                                     CreateExceptionAuditCommand createExceptionAuditCommand,
                                     ObjectMapper objectMapper,
                                     PrincipalCache principalCache,
                                     ActionAuthorizationManager actionAuthorizationManager,
                                     RemoveRoleFromPrincipalCommand removeRoleFromPrincipalCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
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
