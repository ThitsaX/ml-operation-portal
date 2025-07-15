package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContactCommand;
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.ModifyContact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyContactHandler
    extends CoreServicesAuditableUseCase<ModifyContact.Input, ModifyContact.Output>
    implements ModifyContact {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyContactHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ModifyContactCommand modifyContactCommand;

    public ModifyContactHandler(CreateInputAuditCommand createInputAuditCommand,
                                CreateOutputAuditCommand createOutputAuditCommand,
                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                ObjectMapper objectMapper,
                                PrincipalCache principalCache,
                                ModifyContactCommand modifyContactCommand,
                                ParticipantCache participantCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyContactCommand = modifyContactCommand;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        for (Input.ContactInfo contactInfo : input.contactInfoList()) {

            this.modifyContactCommand.execute(
                new ModifyContactCommand.Input(input.participantId(),
                                               contactInfo.contactId(),
                                               contactInfo.name(),
                                               contactInfo.title(),
                                               contactInfo.email(),
                                               contactInfo.mobile(),
                                               contactInfo.contactType()));
        }

        return new ModifyContact.Output(true);
    }

}
