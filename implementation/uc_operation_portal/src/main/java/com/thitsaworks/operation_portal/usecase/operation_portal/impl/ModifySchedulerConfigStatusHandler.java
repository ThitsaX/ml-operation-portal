package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifySchedulerConfigStatusCommand;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.ModifySchedulerConfigStatus;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.SchedulerEngine;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class ModifySchedulerConfigStatusHandler
    extends OperationPortalUseCase<ModifySchedulerConfigStatus.Input, ModifySchedulerConfigStatus.Output>
    implements ModifySchedulerConfigStatus {

    private static final Logger LOG = LoggerFactory.getLogger(ModifySchedulerConfigStatusHandler.class);

    private final ModifySchedulerConfigStatusCommand modifySchedulerConfigStatusCommand;

    private final SchedulerEngine schedulerEngine;

    public ModifySchedulerConfigStatusHandler(PrincipalCache principalCache,
                                              ActionAuthorizationManager actionAuthorizationManager,
                                              ModifySchedulerConfigStatusCommand modifySchedulerConfigStatusCommand,
                                              SchedulerEngine schedulerEngine) {

        super(principalCache,
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
