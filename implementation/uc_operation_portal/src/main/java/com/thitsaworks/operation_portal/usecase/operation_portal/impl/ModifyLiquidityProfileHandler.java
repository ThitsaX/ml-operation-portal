package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfileCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
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

    private final ModifyLiquidityProfileCommand modifyLiquidityProfileCommand;

    public ModifyLiquidityProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                         CreateOutputAuditCommand createOutputAuditCommand,
                                         CreateExceptionAuditCommand createExceptionAuditCommand,
                                         ObjectMapper objectMapper,
                                         PrincipalCache principalCache,
                                         ActionAuthorizationManager actionAuthorizationManager,
                                         ModifyLiquidityProfileCommand modifyLiquidityProfileCommand,
                                         ParticipantCache participantCache) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifyLiquidityProfileCommand = modifyLiquidityProfileCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.modifyLiquidityProfileCommand.execute(new ModifyLiquidityProfileCommand.Input(input.participantId(),
                                                                                               input.liquidityProfileId(),
                                                                                               input.bankName(),
                                                                                               input.accountName(),
                                                                                               input.accountNumber(),
                                                                                               input.currency()));

        return new Output(output.modified());
    }

}
