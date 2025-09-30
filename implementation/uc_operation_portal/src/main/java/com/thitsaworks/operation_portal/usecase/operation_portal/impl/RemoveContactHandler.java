package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.command.CreateContactHistoryCommand;
import com.thitsaworks.operation_portal.core.participant.command.RemoveContactCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveContact;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RemoveContactHandler extends OperationPortalAuditableUseCase<RemoveContact.Input, RemoveContact.Output>
    implements RemoveContact {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveContactHandler.class);

    private final RemoveContactCommand removeContactCommand;

    private final CreateContactHistoryCommand createContactHistoryCommand;

    private final UserPermissionManager userPermissionManager;

    public RemoveContactHandler(CreateInputAuditCommand createInputAuditCommand,
                                CreateOutputAuditCommand createOutputAuditCommand,
                                CreateExceptionAuditCommand createExceptionAuditCommand,
                                ObjectMapper objectMapper,
                                PrincipalCache principalCache,
                                ActionAuthorizationManager actionAuthorizationManager,
                                RemoveContactCommand removeContactCommand,
                                CreateContactHistoryCommand createContactHistoryCommand,
                                UserPermissionManager userPermissionManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.removeContactCommand = removeContactCommand;
        this.createContactHistoryCommand = createContactHistoryCommand;
        this.userPermissionManager = userPermissionManager;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var currentUser = this.userPermissionManager.getCurrentUser();

        if (this.userPermissionManager.isDfsp(currentUser.principalId())) {
            if (!this.userPermissionManager.isSameParticipant(new ParticipantId(currentUser.realmId()
                                                                                           .getId()),
                                                              input.participantId())) {
                throw new IAMException(IAMErrors.UNAUTHORIZED_CREATION);
            }
        }

        this.createContactHistoryCommand.execute(new CreateContactHistoryCommand.Input(
            input.contactId(), input.participantId()));

        var output = this.removeContactCommand.execute(new RemoveContactCommand.Input(input.participantId(),
                                                                                      input.contactId()));

        return new Output(output.removed(), output.contactId());
    }

}
