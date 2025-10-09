package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifySchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.impl.ModifySchedulerConfigCommandHandler;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySchedulerConfig;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModifySchedulerConfigHandler
    extends OperationPortalAuditableUseCase<ModifySchedulerConfig.Input, ModifySchedulerConfig.Output>
    implements ModifySchedulerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySchedulerConfigHandler.class);

    private final ModifySchedulerConfigCommand modifySchedulerConfigCommand;

    private final SchedulerEngine schedulerEngine;

    public ModifySchedulerConfigHandler(CreateInputAuditCommand createInputAuditCommand,
                                        CreateOutputAuditCommand createOutputAuditCommand,
                                        CreateExceptionAuditCommand createExceptionAuditCommand,
                                        SchedulerEngine schedulerEngine,
                                        ObjectMapper objectMapper,
                                        PrincipalCache principalCache,
                                        ModifySchedulerConfigCommand modifySchedulerConfigCommand,
                                        ActionAuthorizationManager actionAuthorizationManager) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.modifySchedulerConfigCommand = modifySchedulerConfigCommand;
        this.schedulerEngine = schedulerEngine;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        var
            output =
                this.modifySchedulerConfigCommand.execute(new ModifySchedulerConfigCommandHandler.Input(input.schedulerConfigId(),
                                                                                                        input.name(),
                                                                                                        input.jobName(),
                                                                                                        input.description(),
                                                                                                        input.cronExpression(),
                                                                                                        input.zoneId().getId(),
                                                                                                        input.active()));
        this.schedulerEngine.scheduleOrReschedule(output.schedulerConfigData());

        return new Output(true);
    }

}
