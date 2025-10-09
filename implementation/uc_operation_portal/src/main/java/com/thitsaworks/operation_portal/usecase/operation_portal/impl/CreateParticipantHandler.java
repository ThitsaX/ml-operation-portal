package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateParticipant;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CreateParticipantHandler
    extends OperationPortalAuditableUseCase<CreateParticipant.Input, CreateParticipant.Output>
    implements CreateParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantHandler.class);

    private final CreateParticipantCommand createParticipantCommand;

    public CreateParticipantHandler(CreateInputAuditCommand createInputAuditCommand,
                                    CreateOutputAuditCommand createOutputAuditCommand,
                                    CreateExceptionAuditCommand createExceptionAuditCommand,
                                    ObjectMapper objectMapper,
                                    PrincipalCache principalCache,
                                    ActionAuthorizationManager actionAuthorizationManager,
                                    CreateParticipantCommand createParticipantCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.createParticipantCommand = createParticipantCommand;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        CreateParticipantCommand.Output output = this.createParticipantCommand.execute(
            new CreateParticipantCommand.Input(input.participantName(),
                                               input.description(),
                                               input.address(),
                                               input.mobile(),
                                               input.status(),
                                               input.contactInfoList()
                                                    .stream()
                                                    .map(info -> new CreateParticipantCommand.Input.ContactInfo(info.name(),
                                                                                                                info.position(),
                                                                                                                info.email(),
                                                                                                                info.mobile(),
                                                                                                                info.contactType()))
                                                    .collect(Collectors.toList()),
                                               input.liquidityProfileInfoList()
                                                    .stream()
                                                    .map(info -> new CreateParticipantCommand.Input.LiquidityProfileInfo(
                                                        info.accountName(),
                                                        info.accountNumber(),
                                                        info.currency(),
                                                        info.status()))
                                                    .collect(Collectors.toList())));

        return new CreateParticipant.Output(output.created(), output.participantId());
    }

}
