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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateNewContactHandler extends OperationPortalAuditableUseCase<CreateNewContact.Input, CreateNewContact.Output>
    implements CreateNewContact {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final CreateContactCommand createContactCommand;

    public CreateNewContactHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   CreateContactCommand createContactCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createContactCommand = createContactCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        for (Input.ContactInfo contactInfo : input.contactInfoList()) {

            this.createContactCommand.execute(new CreateContactCommand.Input(contactInfo.name(),
                                                                             contactInfo.title(),
                                                                             contactInfo.email(),
                                                                             contactInfo.mobile(),
                                                                             input.participantId(),
                                                                             contactInfo.contactType()));
        }

        return new CreateNewContact.Output(true);
    }

}
