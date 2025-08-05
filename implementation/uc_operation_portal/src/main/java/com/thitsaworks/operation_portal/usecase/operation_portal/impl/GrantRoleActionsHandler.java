package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.GrantRoleActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GrantRoleActions;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GrantRoleActionsHandler
    extends OperationPortalAuditableUseCase<GrantRoleActions.Input, GrantRoleActions.Output>
    implements GrantRoleActions {

    private static final Logger LOG = LoggerFactory.getLogger(GrantRoleActionsHandler.class);

    private final GrantRoleActionCommand grantRoleActionCommand;

    public GrantRoleActionsHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   GrantRoleActionCommand grantRoleActionsCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.grantRoleActionCommand = grantRoleActionsCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        for (var singleRoleGrant : input.singleRoleGrantList()) {

            for (var action : singleRoleGrant.actionList()) {
                this.grantRoleActionCommand.execute(new GrantRoleActionCommand.Input(singleRoleGrant.role(),
                                                                                     action));

            }
        }

        return new Output(true);
    }

}
