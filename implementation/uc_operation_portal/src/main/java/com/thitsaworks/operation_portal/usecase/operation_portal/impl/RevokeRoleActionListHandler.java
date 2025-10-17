package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.RevokeRoleActionCommand;
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RevokeRoleActionList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class RevokeRoleActionListHandler
    extends OperationPortalAuditableUseCase<RevokeRoleActionList.Input, RevokeRoleActionList.Output>
    implements RevokeRoleActionList {

    private static final Logger LOG = LoggerFactory.getLogger(RevokeRoleActionListHandler.class);

    private final RevokeRoleActionCommand revokeRoleActionCommand;

    public RevokeRoleActionListHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       RevokeRoleActionCommand revokeRoleActionCommand,
                                       IAMQuery iamQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.revokeRoleActionCommand = revokeRoleActionCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        for (var actionCode : input.actionCodeList()) {

            this.revokeRoleActionCommand.execute(new RevokeRoleActionCommand.Input(input.roleName(), actionCode));
        }

        return new Output(true);
    }

}
