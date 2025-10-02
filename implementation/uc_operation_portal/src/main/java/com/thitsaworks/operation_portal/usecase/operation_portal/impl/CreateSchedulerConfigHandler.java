package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateSchedulerConfig;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateSchedulerConfigHandler
    extends OperationPortalAuditableUseCase<CreateSchedulerConfig.Input, CreateSchedulerConfig.Output>
    implements CreateSchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(CreateSchedulerConfigHandler.class);

    private final CreateSchedulerConfigCommand createSchedulerConfigCommand;

    private final SchedulerEngine schedulerEngine;

    public CreateSchedulerConfigHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        CreateSchedulerConfigCommand createSchedulerConfigCommand,
                                        ActionAuthorizationManager actionAuthorizationManager,
                                        SchedulerEngine schedulerEngine) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
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
                                                                                                 input.cronExpression(),
                                                                                                 input.description(),
                                                                                                 true));

        this.schedulerEngine.scheduleOrReschedule(output.schedulerConfigData());

        return new Output(true);
    }

}
