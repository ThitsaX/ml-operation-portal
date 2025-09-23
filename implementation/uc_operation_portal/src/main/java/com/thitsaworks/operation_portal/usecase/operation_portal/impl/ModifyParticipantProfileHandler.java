package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.security.SecurityContext;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCaseContext;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyParticipantProfile;
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

    private final PrincipalCache principalCache;

    public ModifyParticipantProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ObjectMapper objectMapper,
                                           PrincipalCache principalCache,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           ModifyParticipantCommand modifyParticipantCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyParticipantCommand = modifyParticipantCommand;
        this.principalCache = principalCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        SecurityContext securityContext = (SecurityContext) UseCaseContext.get();

        PrincipalData requestingPrincipalData =
            this.principalCache.get(new AccessKey(securityContext.accessKey()));

        if (requestingPrincipalData == null) {
            throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND.format(securityContext.userId()
                                                                                       .toString()));

        }

        if (!input.participantId()
                  .equals(new ParticipantId(requestingPrincipalData.realmId()
                                                                   .getId()))) {
            throw new IAMException(IAMErrors.UNAUTHORIZED_CREATION);
        }

        var output = this.modifyParticipantCommand.execute(new ModifyParticipantCommand.Input(input.participantId(),
                                                                                              input.description(),
                                                                                              input.address(),
                                                                                              input.mobile(),
                                                                                              input.logoDataType(),
                                                                                              input.logo()));

        return new Output(output.modified(), output.participantId());
    }

    @Override
    protected void beforeExecute(Input input) throws DomainException {

        Input modifiedInput = new Input(input.participantId(),
                                        input.description(),
                                        input.address(),
                                        input.mobile(),
                                        input.logoDataType(),
                                        null);

        super.beforeExecute(modifiedInput);
    }

}
