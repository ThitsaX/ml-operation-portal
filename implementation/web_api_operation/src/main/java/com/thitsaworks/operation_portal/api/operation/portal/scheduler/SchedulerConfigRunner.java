package com.thitsaworks.operation_portal.api.operation.portal.scheduler;

import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
import com.thitsaworks.operation_portal.usecase.operation_portal.InitializeScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the scheduling of tasks based on configurations from the database.
 * Implements delta-based updates to minimize unnecessary task recreation.
 */
@Component
public class SchedulerConfigRunner {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerConfigRunner.class);
    private final SchedulerConfigQuery schedulerConfigQuery;
    private final InitializeScheduledTasks initializeScheduledTasks;
    private final Map<String, String> activeSchedules = new ConcurrentHashMap<>();

    public SchedulerConfigRunner(SchedulerConfigQuery schedulerConfigQuery,
                               InitializeScheduledTasks initializeScheduledTasks) {
        this.schedulerConfigQuery = schedulerConfigQuery;
        this.initializeScheduledTasks = initializeScheduledTasks;
    }

    /**
     * Runs periodically to check for configuration changes and update scheduled tasks accordingly.
     * Only updates tasks that have actually changed.
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    @CoreWriteTransactional
    public void scheduleTasks() {
        try {
            // Get all active scheduler configurations
            List<SchedulerConfigData> configs = schedulerConfigQuery.getSchedulerConfigs(true, null);
            
            // Track changes
            List<InitializeScheduledTasks.Input.ConfigChange> changes = new ArrayList<>();
            
            // Check for new or updated schedules
            configs.forEach(config -> {
                String taskName = config.name();
                String cronExpression = config.cronExpression();
                String currentCron = activeSchedules.get(taskName);
                
                if (currentCron == null) {
                    // New schedule
                    changes.add(new InitializeScheduledTasks.Input.ConfigChange(
                        taskName, 
                        cronExpression, 
                        InitializeScheduledTasks.Input.ChangeType.NEW
                    ));
                    activeSchedules.put(taskName, cronExpression);
                    LOG.info("Scheduling new task: {} with cron: {}", taskName, cronExpression);
                } else if (!currentCron.equals(cronExpression)) {
                    // Updated schedule
                    changes.add(new InitializeScheduledTasks.Input.ConfigChange(
                        taskName, 
                        cronExpression, 
                        InitializeScheduledTasks.Input.ChangeType.UPDATED
                    ));
                    activeSchedules.put(taskName, cronExpression);
                    LOG.info("Updating task: {} with new cron: {}", taskName, cronExpression);
                }
                // No change needed if cron matches
            });

            // Check for removed schedules
            Set<String> currentTaskNames = new HashSet<>();
            configs.forEach(config -> currentTaskNames.add(config.name()));
            
            activeSchedules.keySet().forEach(taskName -> {
                if (!currentTaskNames.contains(taskName)) {
                    changes.add(new InitializeScheduledTasks.Input.ConfigChange(
                        taskName, 
                        null, 
                        InitializeScheduledTasks.Input.ChangeType.REMOVED
                    ));
                    activeSchedules.remove(taskName);
                    LOG.info("Removed task: {}", taskName);
                }
            });

            // Process changes if any
            if (!changes.isEmpty()) {
                InitializeScheduledTasks.Output result = initializeScheduledTasks.execute(
                    new InitializeScheduledTasks.Input(changes)
                );
                
                if (!result.success()) {
                    LOG.error("Failed to process some scheduled task updates: {}", result.message());
                }
            }

        } catch (Exception e) {
            LOG.error("Error in scheduler config runner", e);
        }
    }
    
    /**
     * Gets the currently active schedules
     * @return Map of task names to their cron expressions
     */
    public Map<String, String> getActiveSchedules() {
        return Collections.unmodifiableMap(activeSchedules);
    }
}
