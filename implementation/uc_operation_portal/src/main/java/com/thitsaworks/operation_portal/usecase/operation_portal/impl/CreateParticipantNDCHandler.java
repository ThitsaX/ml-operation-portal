package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CreateParticipantNDCHandler
    extends OperationPortalAuditableUseCase<CreateParticipantNDC.Input, CreateParticipantNDC.Output>
        implements CreateParticipantNDC {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final CreateParticipantNDCCommand createParticipantNDCCommand;

    private final ModifyParticipantNDCCommand modifyParticipantNDCCommand;

    private final ParticipantNDCQuery participantNDCQuery;

    public CreateParticipantNDCHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       CreateParticipantNDCCommand createParticipantNDCCommand,
                                       ModifyParticipantNDCCommand modifyParticipantNDCCommand,
                                       ParticipantNDCQuery participantNDCQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createParticipantNDCCommand = createParticipantNDCCommand;
        this.modifyParticipantNDCCommand = modifyParticipantNDCCommand;
        this.participantNDCQuery = participantNDCQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        //TODO: To call mojaloop api and calculate ndcamount logic

        Optional<ParticipantNDCData> optionalParticipantNDCData = this.participantNDCQuery.get(input.dfspCode(),
                                                                                               input.currency());

        if (optionalParticipantNDCData.isEmpty()) {

            CreateParticipantNDCCommand.Output output =
                    this.createParticipantNDCCommand.execute(new CreateParticipantNDCCommand.Input(input.dfspCode(),
                                                                                                   input.currency(),
                                                                                                   input.ndcPercent(),
                                                                                                   input.ndcAmount()));

            return new Output(output.participantNDCId());

        } else {

            ModifyParticipantNDCCommand.Output output =
                    this.modifyParticipantNDCCommand.execute(new ModifyParticipantNDCCommand.Input(
                            optionalParticipantNDCData.get().participantNDCId(),
                            input.ndcPercent(),
                            input.ndcAmount()));

            return new Output(output.participantNDCId());
        }
    }
}
