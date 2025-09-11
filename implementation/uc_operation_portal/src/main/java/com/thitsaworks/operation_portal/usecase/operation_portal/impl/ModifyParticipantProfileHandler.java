package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
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

        PrincipalData principalData = this.principalCache.get(input.accessKey());

        if (principalData == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND
                    .defaultMessage("System cannot find the participant with provided ID. ["
                            + input.participantId().getId() + "]."));

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId()
                                  .getId()
                                  .equals(input.participantId()
                                               .getId())) {

                throw new IAMException(IAMErrors.UNAUTHORIZED_CREATION);
            }
        }

        var output = this.modifyParticipantCommand.execute(new ModifyParticipantCommand.Input(input.participantId(),
                                                                                              input.description(),
                                                                                              input.address(),
                                                                                              input.mobile(),
                                                                                              input.logoDataType(),
                                                                                              input.logo()));

        return new Output(output.modified(), output.participantId());
    }

}
