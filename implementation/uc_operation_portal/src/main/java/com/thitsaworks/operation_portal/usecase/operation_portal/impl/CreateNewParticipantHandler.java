package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CreateNewParticipantHandler
    extends OperationPortalAuditableUseCase<CreateParticipant.Input, CreateParticipant.Output>
    implements CreateParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewParticipantHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = EnumSet.allOf(UserRoleType.class);

    private final CreateParticipantCommand createParticipantCommand;

    public CreateNewParticipantHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       CreateParticipantCommand createParticipantCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createParticipantCommand = createParticipantCommand;
    }

    @Override
    public Output onExecute(Input input) throws DomainException {

        CreateParticipantCommand.Output output = this.createParticipantCommand.execute(
            new CreateParticipantCommand.Input(input.name(), input.dfspCode(), input.dfspName(),
                                               input.address(),
                                               input.mobile(),
                                               input.logoType(),
                                               input.logo(),
                                               input.contactInfoList()
                                                    .stream()
                                                    .map(info -> new CreateParticipantCommand.Input.ContactInfo(info.name(),
                                                                                                                info.title(),
                                                                                                                info.email(),
                                                                                                                info.mobile(),
                                                                                                                info.contactType()))
                                                    .collect(Collectors.toList()),
                                               input.liquidityProfileInfoList()
                                                    .stream()
                                                    .map(info -> new CreateParticipantCommand.Input.LiquidityProfileInfo(
                                                        info.bankName(),
                                                        info.accountName(),
                                                        info.accountNumber(),
                                                        info.currency(),
                                                        info.isActive()))
                                                    .collect(Collectors.toList())));

        return new CreateParticipant.Output(output.created(), output.participantId());
    }

}
