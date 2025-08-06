package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.command.CreatePrincipalCommand;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantUserCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.OnboardUser;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OnboardUserHandler
        extends OperationPortalAuditableUseCase<OnboardUser.Input, OnboardUser.Output>
        implements OnboardUser {

    private static final Logger LOG = LoggerFactory.getLogger(OnboardUserHandler.class);

    private final CreateParticipantUserCommand createParticipantUserCommand;

    private final CreatePrincipalCommand createPrincipalCommand;

    public OnboardUserHandler(CreateInputAuditCommand createInputAuditCommand,
                              CreateOutputAuditCommand createOutputAuditCommand,
                              CreateExceptionAuditCommand createExceptionAuditCommand,
                              ObjectMapper objectMapper,
                              PrincipalCache principalCache,
                              ActionAuthorizationManager actionAuthorizationManager,
                              CreateParticipantUserCommand createParticipantUserCommand,
                              CreatePrincipalCommand createPrincipalCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.createParticipantUserCommand = createParticipantUserCommand;
        this.createPrincipalCommand = createPrincipalCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        CreateParticipantUserCommand.Output output = this.createParticipantUserCommand.execute(
                new CreateParticipantUserCommand.Input(input.name(), input.email(), input.participantId(),
                                                       input.firstName(), input.lastName(), input.jobTitle()));

        this.createPrincipalCommand.execute(new CreatePrincipalCommand.Input(new PrincipalId(output.participantUserId()
                                                                                                   .getId()),
                                                                             input.password(),
                                                                             new RealmId(input.participantId()
                                                                                              .getId()),
                                                                             input.activeStatus()));

        return new Output(output.created());
    }

}
