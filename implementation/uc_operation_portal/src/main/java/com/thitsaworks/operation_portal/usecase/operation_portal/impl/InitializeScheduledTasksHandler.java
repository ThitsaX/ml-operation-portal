package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.scheduler.command.CancelScheduledTaskCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.InitializeScheduledTasksCommand;
import com.thitsaworks.operation_portal.usecase.operation_portal.InitializeScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles initialization and updates of scheduled tasks based on configuration changes.
 */
@Service
public class InitializeScheduledTasksHandler
    implements InitializeScheduledTasks, UseCase<InitializeScheduledTasks.Input, InitializeScheduledTasks.Output> {

    private static final Logger LOG = LoggerFactory.getLogger(InitializeScheduledTasksHandler.class);
    private final InitializeScheduledTasksCommand initializeScheduledTasksCommand;
    private final CancelScheduledTaskCommand cancelScheduledTaskCommand;

    public InitializeScheduledTasksHandler(InitializeScheduledTasksCommand initializeScheduledTasksCommand,
                                           CancelScheduledTaskCommand cancelScheduledTaskCommand) {
        this.initializeScheduledTasksCommand = initializeScheduledTasksCommand;
        this.cancelScheduledTaskCommand = cancelScheduledTaskCommand;
    }

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {
        if (input == null || input.changes() == null || input.changes().isEmpty()) {
            return new Output(false, "No changes to process");
        }

        LOG.info("Processing {} scheduled task changes", input.changes().size());
        
        try {
            // Group changes by type for better logging and potential batch processing
            var newTasks = input.changes().stream()
                .filter(c -> c.changeType() == Input.ChangeType.NEW)
                .collect(Collectors.toList());
            
            var updatedTasks = input.changes().stream()
                .filter(c -> c.changeType() == Input.ChangeType.UPDATED)
                .collect(Collectors.toList());
            
            var removedTasks = input.changes().stream()
                .filter(c -> c.changeType() == Input.ChangeType.REMOVED)
                .collect(Collectors.toList());
            
            // Log summary of changes
            if (!newTasks.isEmpty()) {
                LOG.info("Scheduling {} new tasks", newTasks.size());
            }
            if (!updatedTasks.isEmpty()) {
                LOG.info("Updating {} existing tasks", updatedTasks.size());
            }
            if (!removedTasks.isEmpty()) {
                LOG.info("Removing {} tasks", removedTasks.size());
            }
            
            // Process all changes
            for (var change : input.changes()) {
                try {
                    switch (change.changeType()) {
                        case NEW, UPDATED -> {
                            initializeScheduledTasksCommand.execute(
                                change.taskName(), 
                                change.cronExpression()
                            );
                            LOG.debug("Processed {} task: {} with cron: {}", 
                                change.changeType().toString().toLowerCase(),
                                change.taskName(),
                                change.cronExpression()
                            );
                        }
                        case REMOVED -> {
                            cancelScheduledTaskCommand.execute(change.taskName());
                            LOG.debug("Removed task: {}", change.taskName());
                        }
                    }
                } catch (Exception e) {
                    LOG.error("Failed to process task {}: {}", change.taskName(), e.getMessage(), e);
                    // Continue with other tasks even if one fails
                }
            }
            
            return new Output(true, String.format("Processed %d task changes", input.changes().size()));
            
        } catch (Exception e) {
            String errorMsg = "Error initializing scheduled tasks: " + e.getMessage();
            LOG.error(errorMsg, e);
            return new Output(false, errorMsg);
        }
    }
}
