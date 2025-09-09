package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.CancelScheduledTaskCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateOrUpdateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ScheduleTaskCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateOrUpdateSchedulerConfigCommandHandler implements CreateOrUpdateSchedulerConfigCommand {

    private final SchedulerConfigRepository schedulerConfigRepository;

    private final CancelScheduledTaskCommand cancelScheduledTaskCommand;

    private final ScheduleTaskCommand scheduleTaskCommand;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {

        SchedulerConfig config = schedulerConfigRepository.findByName(input.name())
                                                 .orElse(new SchedulerConfig());

        config.setName(input.name());
        config.setCronExpression(input.cronExpression());
        config.setDescription(input.description());
        config.setActive(input.active());

        SchedulerConfig savedConfig = schedulerConfigRepository.save(config);

        // Reschedule the task if active
        if (savedConfig.isActive()) {
            rescheduleTask(savedConfig.getName(), savedConfig.getCronExpression());
        } else {
            cancelScheduledTaskCommand.execute(savedConfig.getName());
        }

        return new Output(savedConfig.getCronExpression(), true);
    }

    @CoreWriteTransactional
    public void rescheduleTask(String taskName, String cronExpression) {
        // Delegate task scheduling to SchedulerService
        scheduleTaskCommand.execute(taskName, cronExpression);
    }

}
