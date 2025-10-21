package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.RevokeMenuActionCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RevokeMenuActionList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class RevokeMenuActionListHandler
    extends OperationPortalAuditableUseCase<RevokeMenuActionList.Input, RevokeMenuActionList.Output>
    implements RevokeMenuActionList {

    private static final Logger LOG = LoggerFactory.getLogger(RevokeMenuActionListHandler.class);

    private final RevokeMenuActionCommand revokeMenuActionCommand;

    public RevokeMenuActionListHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       RevokeMenuActionCommand revokeMenuActionCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.revokeMenuActionCommand = revokeMenuActionCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        for (var actionCode : input.actionCodeList()) {

            this.revokeMenuActionCommand.execute(new RevokeMenuActionCommand.Input(input.menuName(), actionCode));
        }

        return new Output(true);
    }

}
