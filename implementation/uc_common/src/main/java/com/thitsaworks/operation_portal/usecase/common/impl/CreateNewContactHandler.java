package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.OperationPortalException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateContact;
import com.thitsaworks.operation_portal.usecase.CommonAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.common.CreateNewContact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateNewContactHandler extends CommonAuditableUseCase<CreateNewContact.Input, CreateNewContact.Output>
    implements CreateNewContact {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final CreateContact createContact;

    public CreateNewContactHandler(CreateInputAuditCommand createInputAuditCommand,
                                   CreateOutputAuditCommand createOutputAuditCommand,
                                   CreateExceptionAuditCommand createExceptionAuditCommand,
                                   ObjectMapper objectMapper,
                                   PrincipalCache principalCache,
                                   CreateContact createContact) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createContact = createContact;
    }

    @Override
    protected Output onExecute(Input input) throws OperationPortalException {

        for (Input.ContactInfo contactInfo : input.contactInfoList()) {

            this.createContact.execute(new CreateContact.Input(contactInfo.name(),
                                                               contactInfo.title(),
                                                               contactInfo.email(),
                                                               contactInfo.mobile(),
                                                               input.participantId(),
                                                               contactInfo.contactType()));
        }

        return new CreateNewContact.Output(true);
    }

}
