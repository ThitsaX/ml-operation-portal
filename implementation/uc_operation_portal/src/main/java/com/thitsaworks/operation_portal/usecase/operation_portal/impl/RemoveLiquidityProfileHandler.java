package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.RemoveLiquidityProfileCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveLiquidityProfile;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RemoveLiquidityProfileHandler
    extends OperationPortalAuditableUseCase<RemoveLiquidityProfile.Input, RemoveLiquidityProfile.Output>
    implements RemoveLiquidityProfile {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveLiquidityProfileHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final RemoveLiquidityProfileCommand removeLiquidityProfileCommand;

    public RemoveLiquidityProfileHandler(CreateInputAuditCommand createInputAuditCommand,
                                         CreateOutputAuditCommand createOutputAuditCommand,
                                         CreateExceptionAuditCommand createExceptionAuditCommand,
                                         ObjectMapper objectMapper,
                                         PrincipalCache principalCache,
                                         ActionAuthorizationManager actionAuthorizationManager,
                                         RemoveLiquidityProfileCommand removeLiquidityProfileCommand) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.removeLiquidityProfileCommand = removeLiquidityProfileCommand;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.removeLiquidityProfileCommand.execute(new RemoveLiquidityProfileCommand.Input(input.participantId(),
                                                                                               input.liquidityProfileId()));

        return new Output(output.removed(), output.liquidityProfileId());
    }

}
