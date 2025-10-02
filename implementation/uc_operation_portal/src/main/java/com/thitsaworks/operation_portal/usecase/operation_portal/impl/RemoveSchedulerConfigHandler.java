package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.DeleteSchedulerConfigCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveSchedulerConfig;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RemoveSchedulerConfigHandler
    extends OperationPortalAuditableUseCase<RemoveSchedulerConfig.Input, RemoveSchedulerConfig.Output>
    implements RemoveSchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveSchedulerConfigHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION, UserRoleType.ADMIN);

    private final DeleteSchedulerConfigCommand deleteSchedulerConfigCommand;

    private final SchedulerEngine schedulerEngine;

    public RemoveSchedulerConfigHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        DeleteSchedulerConfigCommand deleteSchedulerConfigCommand,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        SchedulerEngine schedulerEngine) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.deleteSchedulerConfigCommand = deleteSchedulerConfigCommand;
        this.schedulerEngine = schedulerEngine;
    }

    @Override
    protected RemoveSchedulerConfig.Output onExecute(Input input) throws DomainException {

        LOG.info("Deleting scheduler config with ID: {}", input.schedulerConfigId());

        var output = this.deleteSchedulerConfigCommand.execute(input.schedulerConfigId());

        this.schedulerEngine.cancel(input.schedulerConfigId().getId());

        return new Output(output.deleted());
    }
}