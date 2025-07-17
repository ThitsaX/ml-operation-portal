package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.command.SaveLiquidityProfileCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyLiquidityProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyLiquidityProfileHandler
    extends OperationPortalAuditableUseCase<ModifyLiquidityProfile.Input, ModifyLiquidityProfile.Output>
    implements ModifyLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyLiquidityProfileHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final SaveLiquidityProfileCommand saveLiquidityProfileCommand;

    private final ParticipantCache participantCache;

    public ModifyLiquidityProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                         CreateOutputAuditCommand createOutputAuditCommand,
                                         CreateExceptionAuditCommand createExceptionAuditCommand,
                                         ObjectMapper objectMapper,
                                         PrincipalCache principalCache,
                                         SaveLiquidityProfileCommand saveLiquidityProfileCommand,
                                         ParticipantCache participantCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.saveLiquidityProfileCommand = saveLiquidityProfileCommand;
        this.participantCache = participantCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        if (this.participantCache.get(input.participantId()) == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
        }

        for (ModifyLiquidityProfile.Input.LiquidityProfileInfo profileInfo : input.liquidityProfileInfoList()) {

            this.saveLiquidityProfileCommand.execute(
                new SaveLiquidityProfileCommand.Input(input.participantId(),
                                                      profileInfo.liquidityProfileId(),
                                                      profileInfo.bankName(),
                                                      profileInfo.accountName(),
                                                      profileInfo.accountNumber(),
                                                      profileInfo.currency()));
        }

        return new Output(true);
    }

}
