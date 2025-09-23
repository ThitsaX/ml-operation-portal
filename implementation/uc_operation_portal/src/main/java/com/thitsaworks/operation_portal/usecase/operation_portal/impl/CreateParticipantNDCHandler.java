package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantNDCData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateParticipantNDC;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateParticipantNDCHandler
    extends OperationPortalAuditableUseCase<CreateParticipantNDC.Input, CreateParticipantNDC.Output>
    implements CreateParticipantNDC {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantNDCHandler.class);

    private final CreateParticipantNDCCommand createParticipantNDCCommand;

    private final ModifyParticipantNDCCommand modifyParticipantNDCCommand;

    private final ParticipantNDCQuery participantNDCQuery;

    public CreateParticipantNDCHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       CreateParticipantNDCCommand createParticipantNDCCommand,
                                       ModifyParticipantNDCCommand modifyParticipantNDCCommand,
                                       ParticipantNDCQuery participantNDCQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.createParticipantNDCCommand = createParticipantNDCCommand;
        this.modifyParticipantNDCCommand = modifyParticipantNDCCommand;
        this.participantNDCQuery = participantNDCQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        //TODO: To call mojaloop api and calculate ndcamount logic

        Optional<ParticipantNDCData> optionalParticipantNDCData = this.participantNDCQuery.get(input.participantName(),
                                                                                               input.currency());

        if (optionalParticipantNDCData.isEmpty()) {

            CreateParticipantNDCCommand.Output output =
                this.createParticipantNDCCommand.execute(new CreateParticipantNDCCommand.Input(input.participantName(),
                                                                                               input.currency(),
                                                                                               input.ndcPercent()));

            return new Output(output.participantNDCId());

        } else {

            ModifyParticipantNDCCommand.Output output =
                this.modifyParticipantNDCCommand.execute(new ModifyParticipantNDCCommand.Input(
                    optionalParticipantNDCData.get()
                                              .participantNDCId(), input.ndcPercent()));

            return new Output(output.participantNDCId());
        }
    }

}
