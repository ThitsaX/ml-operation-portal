package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateContactHistoryCommand;
import com.thitsaworks.operation_portal.core.participant.command.RemoveContactCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveContact;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RemoveContactHandler extends OperationPortalAuditableUseCase<RemoveContact.Input, RemoveContact.Output>
    implements RemoveContact {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveContactHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final RemoveContactCommand removeContactCommand;

    private final CreateContactHistoryCommand createContactHistoryCommand;

    public RemoveContactHandler(CreateInputAuditCommand createInputAuditCommand,
                                CreateOutputAuditCommand createOutputAuditCommand,
                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                ObjectMapper objectMapper,
                                PrincipalCache principalCache,
                                ActionAuthorizationManager actionAuthorizationManager,
                                RemoveContactCommand removeContactCommand,
                                CreateContactHistoryCommand createContactHistoryCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.removeContactCommand = removeContactCommand;
        this.createContactHistoryCommand = createContactHistoryCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        this.createContactHistoryCommand.execute(new CreateContactHistoryCommand.Input(
            input.contactId(), input.participantId()));

        var output = this.removeContactCommand.execute(new RemoveContactCommand.Input(input.participantId(),
                                                                                      input.contactId()));

        return new Output(output.removed(), output.contactId());
    }

}
