package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.SaveLiquidityProfileCommand;
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.SaveLiquidityProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SaveLiquidityProfileHandler
    extends CoreServicesAuditableUseCase<SaveLiquidityProfile.Input, SaveLiquidityProfile.Output>
    implements SaveLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(SaveLiquidityProfileHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final SaveLiquidityProfileCommand saveLiquidityProfileCommand;

    public SaveLiquidityProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       SaveLiquidityProfileCommand saveLiquidityProfileCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.saveLiquidityProfileCommand = saveLiquidityProfileCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.saveLiquidityProfileCommand.execute(new SaveLiquidityProfileCommand.Input(input.participantId(),
                                                                                           input.liquidityProfileId(),
                                                                                           input.bankName(),
                                                                                           input.accountName(),
                                                                                           input.accountNumber(),
                                                                                           input.currency()));

        return new Output(output.saved(), output.liquidityProfileId());
    }

}
