package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfileCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.ModifyLiquidityProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyLiquidityProfileHandler
    extends CoreServicesAuditableUseCase<ModifyLiquidityProfile.Input, ModifyLiquidityProfile.Output>
    implements ModifyLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyLiquidityProfileHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ModifyLiquidityProfileCommand modifyLiquidityProfileCommand;

    private final ParticipantCache participantCache;

    public ModifyLiquidityProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                         CreateOutputAuditCommand createOutputAuditCommand,
                                         CreateExceptionAuditCommand createExceptionAuditCommand,
                                         ObjectMapper objectMapper,
                                         PrincipalCache principalCache,
                                         ModifyLiquidityProfileCommand modifyLiquidityProfileCommand,
                                         ParticipantCache participantCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyLiquidityProfileCommand = modifyLiquidityProfileCommand;
        this.participantCache = participantCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        if (this.participantCache.get(input.participantId()) == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
        }

        for (ModifyLiquidityProfile.Input.LiquidityProfileInfo profileInfo : input.liquidityProfileInfoList()) {

            this.modifyLiquidityProfileCommand.execute(
                new ModifyLiquidityProfileCommand.Input(input.participantId(),
                                                        profileInfo.liquidityProfileId(),
                                                        profileInfo.bankName(),
                                                        profileInfo.accountName(),
                                                        profileInfo.accountNumber(),
                                                        profileInfo.currency(),
                                                        profileInfo.isActive()));
        }

        return new Output(true);
    }

}
