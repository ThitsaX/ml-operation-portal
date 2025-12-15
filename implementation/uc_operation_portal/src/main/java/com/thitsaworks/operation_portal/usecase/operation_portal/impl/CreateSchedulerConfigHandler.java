package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSchedulerConfig;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateSchedulerConfigHandler
    extends OperationPortalUseCase<CreateSchedulerConfig.Input, CreateSchedulerConfig.Output>
    implements CreateSchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSchedulerConfigHandler.class);

    private final CreateSchedulerConfigCommand createSchedulerConfigCommand;

    private final SchedulerEngine schedulerEngine;

    public CreateSchedulerConfigHandler(PrincipalCache principalCache,
                                        CreateSchedulerConfigCommand createSchedulerConfigCommand,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        SchedulerEngine schedulerEngine) {

        super(principalCache,
              actionAuthorizationManager);

        this.createSchedulerConfigCommand = createSchedulerConfigCommand;
        this.schedulerEngine = schedulerEngine;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
            this.createSchedulerConfigCommand.execute(new CreateSchedulerConfigCommand.Input(input.name(),
                                                                                             input.jobName(),
                                                                                             input.description(),
                                                                                             input.cronExpression(),
                                                                                             input.zoneId()
                                                                                                  .getId()));

        this.schedulerEngine.scheduleOrReschedule(output.schedulerConfigData());

        return new Output(true);
    }

}
