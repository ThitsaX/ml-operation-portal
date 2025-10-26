package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifySchedulerConfigStatusCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySchedulerConfigStatus;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class ModifySchedulerConfigStatusHandler
        extends OperationPortalAuditableUseCase<ModifySchedulerConfigStatus.Input, ModifySchedulerConfigStatus.Output>
        implements ModifySchedulerConfigStatus {

    private static final Logger logger = LoggerFactory.getLogger(ModifySchedulerConfigStatusHandler.class);

    private final ModifySchedulerConfigStatusCommand modifySchedulerConfigStatusCommand;

    private final SchedulerEngine schedulerEngine;

    public ModifySchedulerConfigStatusHandler(
            CreateInputAuditCommand createInputAuditCommand,
            CreateOutputAuditCommand createOutputAuditCommand,
            CreateExceptionAuditCommand createExceptionAuditCommand,
            ObjectMapper objectMapper,
            PrincipalCache principalCache,
            ActionAuthorizationManager actionAuthorizationManager,
            ModifySchedulerConfigStatusCommand modifySchedulerConfigStatusCommand, SchedulerEngine schedulerEngine) {

        super(createInputAuditCommand, createOutputAuditCommand, createExceptionAuditCommand, objectMapper,
                principalCache,
                actionAuthorizationManager);
        this.modifySchedulerConfigStatusCommand = modifySchedulerConfigStatusCommand;
        this.schedulerEngine = schedulerEngine;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var output = this.modifySchedulerConfigStatusCommand.execute(
                new ModifySchedulerConfigStatusCommand.Input(input.schedulerConfigId(), input.active()));

        this.schedulerEngine.scheduleOrReschedule(output.schedulerConfigData());

        return new Output(output.modified(), output.schedulerConfigData());
    }

}
