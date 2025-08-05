package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.GrantPrincipalActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantUserAction;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GrantUserActionHandler
    extends OperationPortalAuditableUseCase<GrantUserAction.Input,GrantUserAction.Output>
    implements GrantUserAction {

    private final GrantPrincipalActionCommand grantPrincipalActionCommand;

    public GrantUserActionHandler(CreateInputAuditCommand createInputAuditCommand,
                                  CreateOutputAuditCommand createOutputAuditCommand,
                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                  ObjectMapper objectMapper,
                                  PrincipalCache principalCache,
                                  ActionAuthorizationManager actionAuthorizationManager,
                                  GrantPrincipalActionCommand grantPrincipalActionCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);
        this.grantPrincipalActionCommand = grantPrincipalActionCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {



            for (var userGrant : input.userGrantList()) {
                this.grantPrincipalActionCommand.execute(
                    new GrantPrincipalActionCommand.Input(userGrant.principalId(), userGrant.actionId()));
            }

            return new Output(true);


    }

}