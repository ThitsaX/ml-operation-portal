package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateContactCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateNewContact;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateNewContactHandler
    extends OperationPortalAuditableUseCase<CreateNewContact.Input, CreateNewContact.Output>
    implements CreateNewContact {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactHandler.class);

    private final CreateContactCommand createContactCommand;

    public CreateNewContactHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   ActionAuthorizationManager actionAuthorizationManager,
                                   CreateContactCommand createContactCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.createContactCommand = createContactCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output = this.createContactCommand.execute(new CreateContactCommand.Input(input.participantId(),
                                                                                      input.name(),
                                                                                      input.position(),
                                                                                      input.email(),
                                                                                      input.mobile(),
                                                                                      input.contactType()));

        return new CreateNewContact.Output(output.created(), output.contactId());
    }

}
