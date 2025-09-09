package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.ScheduleTaskCommand;
import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.JobExecutionLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@EnableAsync
public class ScheduleTaskCommandHandler implements ScheduleTaskCommand {

    private final Map<String, ScheduledFuture<?>> scheduledTasks;
    private final ScheduledExecutorService taskScheduler = Executors.newScheduledThreadPool(5, r -> {
        Thread t = new Thread(r, "scheduler-thread");
        t.setDaemon(true);
        return t;
    });
    private final JobExecutionLogRepository logRepository;
    private final TransactionTemplate transactionTemplate;
    
    public ScheduleTaskCommandHandler(JobExecutionLogRepository logRepository, 
                                   Map<String, ScheduledFuture<?>> scheduledTasks,
                                   @Qualifier(PersistenceQualifiers.Core.TRANSACTION_MANAGER)
                                   PlatformTransactionManager transactionManager) {
        this.logRepository = logRepository;
        this.scheduledTasks = scheduledTasks;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }
    
    @Override
    @CoreWriteTransactional
    public void destroy() {
        // Shutdown the scheduler when the application context is closed
        taskScheduler.shutdown();
        try {
            if (!taskScheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                taskScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            taskScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    @CoreWriteTransactional
    public Output execute(String taskName, String cronExpression) {
        try {
            // Validate cron expression
            CronExpression.parse(cronExpression);

            // Cancel existing task if it exists
            ScheduledFuture<?> existingTask = scheduledTasks.remove(taskName);
            if (existingTask != null) {
                existingTask.cancel(false);
            }

            // Calculate initial delay
            long initialDelay = calculateNextExecution(cronExpression);
            
            // Schedule the task with the cron expression
            ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                new ScheduledTaskWrapper(this, taskName, cronExpression),
                initialDelay,
                TimeUnit.MILLISECONDS
            );
            
            scheduledTasks.put(taskName, scheduledTask);
            return new Output(true);
            
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid cron expression: " + cronExpression, e);
        }
    }
    

    @Async
    public void executeScheduledTask(String taskName) {
        transactionTemplate.execute(status -> {
            LocalDateTime startTime = LocalDateTime.now();
            JobExecutionLog log = new JobExecutionLog(taskName, "STARTED", startTime);
            
            try {
                // Simulate some work
                Thread.sleep(2000); // 2 seconds of work
                
                // Update log with success status
                log.setStatus("COMPLETED");
                log.setExecutionMessage("Job executed successfully at " + LocalDateTime.now());
                return null;
            } catch (Exception e) {
                log.setStatus("FAILED");
                log.setExecutionMessage("Error: " + e.getMessage());
                return null;
            } finally {
                log.setEndTime(LocalDateTime.now());
                // Save log in the same transaction
                logRepository.save(log);
            }
        });
    }

    private long calculateNextExecution(String cronExpression) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = CronExpression.parse(cronExpression).next(now);
        if (next == null) {
            throw new IllegalArgumentException("Could not calculate next execution time for: " + cronExpression);
        }
        return Duration.between(now, next).toMillis();
    }

    private class ScheduledTaskWrapper implements Runnable {
        private final ScheduleTaskCommandHandler scheduler;
        private final String taskName;
        private final String cronExpression;
        private LocalDateTime nextExecution;

        public ScheduledTaskWrapper(ScheduleTaskCommandHandler scheduler, String taskName, String cronExpression) {
            this.scheduler = scheduler;
            this.taskName = taskName;
            this.cronExpression = cronExpression;
            this.nextExecution = LocalDateTime.now();
        }

        @Override
        public void run() {
            try {
                // Execute the task
                scheduler.executeScheduledTask(taskName);

                // Schedule next execution
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime next = CronExpression.parse(cronExpression)
                                                 .next(now);
                if (next == null) {
                    throw new IllegalStateException("No next execution time for cron: " + cronExpression);
                }

                long delay = Duration.between(now, next).toMillis();

                // Reschedule for next execution
                ScheduledFuture<?> scheduledTask = taskScheduler.schedule(
                    this,
                    delay,
                    TimeUnit.MILLISECONDS
                );
                scheduledTasks.put(taskName, scheduledTask);

                this.nextExecution = next;
            } catch (Exception e) {
                // Log error and don't reschedule
                System.err.println("Error in scheduled task " + taskName + ": " + e.getMessage());
            }
        }
    }

}
