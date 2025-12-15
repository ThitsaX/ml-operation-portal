package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.DeleteSchedulerConfigCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.RemoveSchedulerConfig;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RemoveSchedulerConfigHandler
    extends OperationPortalUseCase<RemoveSchedulerConfig.Input, RemoveSchedulerConfig.Output>
    implements RemoveSchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveSchedulerConfigHandler.class);

    private final DeleteSchedulerConfigCommand deleteSchedulerConfigCommand;

    private final SchedulerEngine schedulerEngine;

    public RemoveSchedulerConfigHandler(PrincipalCache principalCache,
                                        DeleteSchedulerConfigCommand deleteSchedulerConfigCommand,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        SchedulerEngine schedulerEngine) {

        super(principalCache,
              actionAuthorizationManager);

        this.deleteSchedulerConfigCommand = deleteSchedulerConfigCommand;
        this.schedulerEngine = schedulerEngine;
    }

    @Override
    protected RemoveSchedulerConfig.Output onExecute(Input input) throws DomainException {

        LOG.info("Deleting scheduler config with ID: {}", input.schedulerConfigId());

        var output = this.deleteSchedulerConfigCommand.execute(input.schedulerConfigId());

        this.schedulerEngine.cancel(input.schedulerConfigId()
                                         .getId());

        return new Output(output.deleted());
    }

}