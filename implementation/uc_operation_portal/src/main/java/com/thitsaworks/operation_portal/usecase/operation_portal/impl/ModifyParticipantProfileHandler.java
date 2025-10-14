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
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyParticipantProfile;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifyParticipantProfileHandler
    extends OperationPortalAuditableUseCase<ModifyParticipantProfile.Input, ModifyParticipantProfile.Output>
    implements ModifyParticipantProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantProfileHandler.class);

    private final ModifyParticipantCommand modifyParticipantCommand;

    private final UserPermissionManager userPermissionManager;

    public ModifyParticipantProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           ModifyParticipantCommand modifyParticipantCommand,
                                           UserPermissionManager userPermissionManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyParticipantCommand = modifyParticipantCommand;
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

        var output = this.modifyParticipantCommand.execute(new ModifyParticipantCommand.Input(input.participantId(),
                                                                                              input.description(),
                                                                                              input.address(),
                                                                                              input.mobile(),
                                                                                              input.logoFileType(),
                                                                                              input.logo()));

        return new Output(output.modified(), output.participantId());
    }

    @Override
    protected void beforeExecute(Input input) throws DomainException {

        Input modifiedInput = new Input(input.participantId(),
                                        input.description(),
                                        input.address(),
                                        input.mobile(),
                                        input.logoFileType(),
                                        null);

        super.beforeExecute(modifiedInput);
    }

}
