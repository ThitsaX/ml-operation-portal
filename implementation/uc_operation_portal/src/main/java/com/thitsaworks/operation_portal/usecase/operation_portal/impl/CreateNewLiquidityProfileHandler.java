package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateLiquidityProfileCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateNewLiquidityProfile;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CreateNewLiquidityProfileHandler
    extends OperationPortalAuditableUseCase<CreateNewLiquidityProfile.Input, CreateNewLiquidityProfile.Output>
    implements CreateNewLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewLiquidityProfileHandler.class);

    private final CreateLiquidityProfileCommand createLiquidityProfileCommand;

    public CreateNewLiquidityProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            ObjectMapper objectMapper,
                                            PrincipalCache principalCache,
                                            ActionAuthorizationManager actionAuthorizationManager,
                                            CreateLiquidityProfileCommand createLiquidityProfileCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.createLiquidityProfileCommand = createLiquidityProfileCommand;

    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.createLiquidityProfileCommand.execute(new CreateLiquidityProfileCommand.Input(input.participantId(),
                                                                                               input.bankName(),
                                                                                               input.accountName(),
                                                                                               input.accountNumber(),
                                                                                               input.currency(),
                                                                                               true));

        return new Output(true, output.liquidityProfileId());
    }

}
