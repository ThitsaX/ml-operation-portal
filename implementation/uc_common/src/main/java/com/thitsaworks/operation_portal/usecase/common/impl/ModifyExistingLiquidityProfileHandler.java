package com.thitsaworks.operation_portal.usecase.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.usecase.CommonAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.common.ModifyExistingLiquidityProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ModifyExistingLiquidityProfileHandler
    extends CommonAuditableUseCase<ModifyExistingLiquidityProfile.Input, ModifyExistingLiquidityProfile.Output>
    implements ModifyExistingLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyExistingLiquidityProfileHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ModifyLiquidityProfile modifyLiquidityProfile;

    private final ParticipantCache participantCache;

    public ModifyExistingLiquidityProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                                 CreateOutputAuditCommand createOutputAuditCommand,
                                                 CreateExceptionAuditCommand createExceptionAuditCommand,
                                                 ObjectMapper objectMapper,
                                                 PrincipalCache principalCache,
                                                 ModifyLiquidityProfile modifyLiquidityProfile,
                                                 ParticipantCache participantCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.modifyLiquidityProfile = modifyLiquidityProfile;
        this.participantCache = participantCache;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        if (this.participantCache.get(input.participantId()) == null) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
        }

        for (ModifyExistingLiquidityProfile.Input.LiquidityProfileInfo profileInfo : input.liquidityProfileInfoList()) {

            this.modifyLiquidityProfile.execute(
                new ModifyLiquidityProfile.Input(input.participantId(),
                                                 profileInfo.liquidityProfileId(),
                                                 profileInfo.accountName(),
                                                 profileInfo.accountNumber(),
                                                 profileInfo.currency(),
                                                 profileInfo.isActive()));
        }

        return new Output(true);
    }

}
