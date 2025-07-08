package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContactCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.usecase.CommonAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingContact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyExistingContactHandler
    extends CommonAuditableUseCase<ModifyExistingContact.Input, ModifyExistingContact.Output>
    implements ModifyExistingContact {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingContactHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ModifyContactCommand modifyContactCommand;

    private final ParticipantCache participantCache;

    public ModifyExistingContactHandler(CreateInputAuditCommand createInputAuditCommand,
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
        this.participantCache = participantCache;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        if (this.participantCache.get(input.participantId()) == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
        }

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

        return new ModifyExistingContact.Output(true);
    }

}
