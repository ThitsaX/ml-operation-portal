package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.test_iam.command.AssignRoleToUserCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.AssignRoleToUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Set;

@Service
public class AssignRoleToUserHandler
    extends OperationPortalAuditableUseCase<AssignRoleToUser.Input, AssignRoleToUser.Output>
    implements AssignRoleToUser {

    private static final Logger LOG = LoggerFactory.getLogger(AssignRoleToUserHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.ADMIN);

    private final AssignRoleToUserCommand assignRoleToUserCommand;

    public AssignRoleToUserHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   AssignRoleToUserCommand assignRoleToUserCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.assignRoleToUserCommand = assignRoleToUserCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.assignRoleToUserCommand.execute(new AssignRoleToUserCommand.Input(input.userId(),
                                                                                            input.roleId()));

        return new Output(output.userRoleId());
    }

}
