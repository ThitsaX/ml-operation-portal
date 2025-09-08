package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.scheduler.command.InitializeScheduledTasksCommand;
import com.thitsaworks.operation_portal.usecase.operation_portal.InitializeScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Handler for initializing scheduled tasks.
 */
@Service
public class InitializeScheduledTasksHandler
    implements InitializeScheduledTasks, UseCase<InitializeScheduledTasks.Input, InitializeScheduledTasks.Output> {

    private static final Logger LOG = LoggerFactory.getLogger(InitializeScheduledTasksHandler.class);

    private final InitializeScheduledTasksCommand initializeScheduledTasksCommand;

    public InitializeScheduledTasksHandler(InitializeScheduledTasksCommand initializeScheduledTasksCommand) {

        this.initializeScheduledTasksCommand = initializeScheduledTasksCommand;
    }

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {
        LOG.info("Initializing scheduled tasks");
        try {
            var output = this.initializeScheduledTasksCommand.execute();
            return new Output(output.initialized());
        } catch (Exception e) {
            LOG.error("Error initializing scheduled tasks", e);
            throw new RuntimeException("Failed to initialize scheduled tasks", e);
        }
    }
}
