package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
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
import com.thitsaworks.operation_portal.usecase.CommonAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ModifyExistingParticipantHandler
    extends CommonAuditableUseCase<ModifyExistingParticipant.Input, ModifyExistingParticipant.Output>
    implements ModifyExistingParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingParticipantHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ModifyParticipantCommand modifyParticipantCommand;

    private final PrincipalCache principalCache;

    public ModifyExistingParticipantHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            ObjectMapper objectMapper,
                                            PrincipalCache principalCache,
                                            ModifyParticipantCommand modifyParticipantCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyParticipantCommand = modifyParticipantCommand;
        this.principalCache = principalCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        PrincipalData principalData = this.principalCache.get(input.accessKey());

        if (principalData == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);

        } else {

            if (principalData.realmId() != null &&
                    !principalData.realmId()
                                  .getId()
                                  .equals(input.participantId()
                                               .getId())) {

                throw new IAMException(IAMErrors.UNAUTHORIZED_CREATION);
            }
        }

        ModifyParticipantCommand.Output output = this.modifyParticipantCommand.execute(
            new ModifyParticipantCommand.Input(input.participantId(),
                                               input.companyName(),
                                               input.address(),
                                               input.mobile(),
                                               input.contactInfoList()
                                             .stream()
                                             .map(info -> new ModifyParticipantCommand.Input.ContactInfo(info.contactId(),
                                                                                                         info.name(),
                                                                                                         info.title(),
                                                                                                         info.email(),
                                                                                                         info.mobile(),
                                                                                                         info.contactType()))
                                             .collect(Collectors.toList()),
                                               input.liquidityProfileInfoList()
                                             .stream()
                                             .map(info -> new ModifyParticipantCommand.Input.LiquidityProfileInfo(info.liquidityProfileId(),
                                                                                                                  info.accountName(),
                                                                                                                  info.accountNumber(),
                                                                                                                  info.currency(),
                                                                                                                  info.isActive()))
                                             .collect(Collectors.toList())));

        return new ModifyExistingParticipant.Output(output.modified(), output.participantId());
    }

}
