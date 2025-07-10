package com.thitsaworks.operation_portal.usecase.core_services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateLiquidityProfileCommand;
import com.thitsaworks.operation_portal.usecase.CoreServicesAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.CreateNewLiquidityProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateNewLiquidityProfileHandler
    extends CoreServicesAuditableUseCase<CreateNewLiquidityProfile.Input, CreateNewLiquidityProfile.Output>
    implements CreateNewLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewLiquidityProfileHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final CreateLiquidityProfileCommand createLiquidityProfileCommand;

    public CreateNewLiquidityProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            ObjectMapper objectMapper,
                                            PrincipalCache principalCache,
                                            CreateLiquidityProfileCommand createLiquidityProfileCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createLiquidityProfileCommand = createLiquidityProfileCommand;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        for (CreateNewLiquidityProfile.Input.LiquidityProfileInfo profileInfo : input.liquidityProfileInfoList()) {

            this.createLiquidityProfileCommand.execute(
                new CreateLiquidityProfileCommand.Input(input.participantId(),
                                                        profileInfo.accountName(),
                                                        profileInfo.accountNumber(),
                                                        profileInfo.currency(),
                                                        profileInfo.isActive()));

        }

        return new Output(true);
    }

}
