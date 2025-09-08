package com.thitsaworks.operation_portal.api.operation.portal.scheduler;

import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.query.SchedulerConfigQuery;
import com.thitsaworks.operation_portal.usecase.operation_portal.InitializeScheduledTasks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * Run every minute to check for new or updated scheduler configurations
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    @CoreWriteTransactional
    public void scheduleTasks() {
        try {
            // Get all active scheduler configurations
            List<SchedulerConfigData> configs = schedulerConfigQuery.getSchedulerConfigs(true, null);

            // Schedule new or updated configs
            configs.forEach(config -> {
                String taskName = config.name();
                String cronExpression = config.cronExpression();

                // Check if this is a new or updated schedule
                if (!cronExpression.equals(activeSchedules.get(taskName))) {
                    try {
                        // Initialize the scheduled tasks using the use case
                        initializeScheduledTasks.execute(new InitializeScheduledTasks.Input());
                        activeSchedules.put(taskName, cronExpression);
                        LOG.info("Initialized scheduled task: {} with cron: {}", taskName, cronExpression);
                    } catch (Exception e) {
                        LOG.error("Failed to schedule task: " + taskName, e);
                    }
                }
            });

            // Handle removed configs
            activeSchedules.keySet().removeIf(taskName ->
                                                  configs.stream().noneMatch(config -> config.name().equals(taskName))
                                             );

        } catch (Exception e) {
            LOG.error("Error in scheduler config runner", e);
        }
    }
}
