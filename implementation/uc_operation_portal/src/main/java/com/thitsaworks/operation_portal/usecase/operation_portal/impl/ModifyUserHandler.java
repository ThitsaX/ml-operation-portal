package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.ModifyPrincipalStatusCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyUserCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifyUserHandler
    extends OperationPortalAuditableUseCase<ModifyUser.Input, ModifyUser.Output>
        implements ModifyUser {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyUserHandler.class);

    private final ModifyUserCommand modifyUserCommand;

    private final ModifyPrincipalStatusCommand modifyPrincipalStatusCommand;

    public ModifyUserHandler(CreateInputAuditCommand createInputAuditCommand,
                             CreateOutputAuditCommand createOutputAuditCommand,
                             CreateExceptionAuditCommand createExceptionAuditCommand,
                             ObjectMapper objectMapper,
                             PrincipalCache principalCache,
                             ActionAuthorizationManager actionAuthorizationManager,
                             ModifyUserCommand modifyUserCommand,
                             ModifyPrincipalStatusCommand modifyPrincipalStatusCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyUserCommand = modifyUserCommand;
        this.modifyPrincipalStatusCommand = modifyPrincipalStatusCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        ModifyUserCommand.Output output =
                this.modifyUserCommand.execute(new ModifyUserCommand.Input(
                        input.userId(),
                        input.name(),
                        null,
                        input.firstName(), input.lastName(), input.jobTitle(), null));

        this.modifyPrincipalStatusCommand.execute(
                new ModifyPrincipalStatusCommand.Input(new PrincipalId(output.userId().getId()),
                                                       input.principalStatus()));

        return new Output(output.modified(), output.userId());

    }

}