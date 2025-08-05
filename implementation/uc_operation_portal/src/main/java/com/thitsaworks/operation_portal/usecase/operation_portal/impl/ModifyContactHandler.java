package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateContactHistoryCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContactCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyContact;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifyContactHandler
    extends OperationPortalAuditableUseCase<ModifyContact.Input, ModifyContact.Output>
    implements ModifyContact {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyContactHandler.class);

    private final ModifyContactCommand modifyContactCommand;

    private final CreateContactHistoryCommand createContactHistoryCommand;

    public ModifyContactHandler(CreateInputAuditCommand createInputAuditCommand,
                                CreateOutputAuditCommand createOutputAuditCommand,
                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                ObjectMapper objectMapper,
                                PrincipalCache principalCache,
                                ActionAuthorizationManager actionAuthorizationManager,
                                ModifyContactCommand modifyContactCommand,
                                ParticipantCache participantCache,
                                CreateContactHistoryCommand createContactHistoryCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyContactCommand = modifyContactCommand;

        this.createContactHistoryCommand = createContactHistoryCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        this.createContactHistoryCommand.execute(new CreateContactHistoryCommand.Input(
            input.contactId(),
            input.participantId()));

        var output = this.modifyContactCommand.execute(new ModifyContactCommand.Input(input.participantId(),
                                                                                      input.contactId(),
                                                                                      input.name(),
                                                                                      input.position(),
                                                                                      input.email(),
                                                                                      input.mobile(),
                                                                                      input.contactType()));

        return new ModifyContact.Output(output.modified());
    }

}
