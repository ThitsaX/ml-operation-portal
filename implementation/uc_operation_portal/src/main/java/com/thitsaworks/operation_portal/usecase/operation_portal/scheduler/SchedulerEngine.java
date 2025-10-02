package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler;

import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SchedulerEngine {

    private final ThreadPoolTaskScheduler taskScheduler;

    private final ApplicationContext applicationContext;

    private final SchedulerConfigRepository schedulerConfigRepository;

    private final ConcurrentMap<Long, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerEngine.class);

    public void bootstrap() {

        this.cancelAll();
        this.refreshAllActive();
    }

    public synchronized void scheduleOrReschedule(SchedulerConfigData schedulerConfigData) {

        cancel(schedulerConfigData.schedulerConfigId().getId());

        if (!schedulerConfigData.active()) {
            return;
        }

        // Validate cron first
        CronExpression expr = CronExpression.parse(schedulerConfigData.cronExpression());

        ZoneId zone = ZoneId.systemDefault();

        ScheduledJob job = applicationContext.getBean(schedulerConfigData.jobName(), ScheduledJob.class);

        CronTrigger trigger = new CronTrigger(expr.toString(), zone);
        ScheduledFuture<?> future = taskScheduler.schedule(scheduledJobWrapper(job, schedulerConfigData), trigger);
        if (future == null) {
            throw new IllegalStateException("Failed to schedule config " + schedulerConfigData);
        }
        futures.put(schedulerConfigData.schedulerConfigId().getId(), future);
    }

    public synchronized void cancel(Long schedulerConfigId) {

        ScheduledFuture<?> f = futures.remove(schedulerConfigId);
        if (f != null) {
            f.cancel(false);
        }
    }

    private Runnable scheduledJobWrapper(ScheduledJob job, SchedulerConfigData schedulerConfigData) {

        return () -> {
            try {
                job.run(schedulerConfigData);
            } catch (Exception ex) {
                LOG.error("Job [{}] failed", schedulerConfigData.jobName(), ex);
            }
        };
    }

    public synchronized void refreshAllActive() {

        Set<SchedulerConfigData> keep = schedulerConfigRepository.findByActiveTrue()
                                                                 .stream()
                                                                 .map(SchedulerConfigData::new)
                                                                 .collect(Collectors.toSet());
        new HashSet<>(futures.keySet()).stream()
                                       .filter(id -> keep.stream()
                                                         .noneMatch(configData -> configData.schedulerConfigId().getId()
                                                                                            .equals(id)))
                                       .forEach(this::cancel);

        // Ensure all active are scheduled
        keep.forEach(this::scheduleOrReschedule);
    }

    public synchronized void cancelAll() {

        new ArrayList<>(futures.keySet()).forEach(this::cancel);
    }

}

