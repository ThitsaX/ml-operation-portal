package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.SaveContactCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.SaveContact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SaveContactHandler extends OperationPortalAuditableUseCase<SaveContact.Input, SaveContact.Output>
    implements SaveContact {

    private static final Logger LOG = LoggerFactory.getLogger(SaveContactHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final SaveContactCommand saveContactCommand;

    public SaveContactHandler(CreateInputAuditCommand createInputAuditCommand,
                              CreateOutputAuditCommand createOutputAuditCommand,
                              CreateExceptionAuditCommand createExceptionAuditCommand,
                              ObjectMapper objectMapper,
                              PrincipalCache principalCache,
                              SaveContactCommand saveContactCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.saveContactCommand = saveContactCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var output = this.saveContactCommand.execute(new SaveContactCommand.Input(input.participantId(),
                                                                                  input.contactId(),
                                                                                  input.name(),
                                                                                  input.title(),
                                                                                  input.email(),
                                                                                  input.mobile(),
                                                                                  input.contactType()));

        return new Output(output.saved(), output.contactId());
    }

}
