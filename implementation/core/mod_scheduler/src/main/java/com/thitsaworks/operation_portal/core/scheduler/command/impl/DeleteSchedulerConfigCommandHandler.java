package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.DeleteSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ScheduleTaskCommand;
import com.thitsaworks.operation_portal.core.scheduler.exception.ResourceNotFoundException;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSchedulerConfigCommandHandler implements DeleteSchedulerConfigCommand {

    private final SchedulerConfigRepository schedulerConfigRepository;

    private final ScheduledAnnotationBeanPostProcessor scheduler;

    private final ScheduleTaskCommand schedulerTaskCommand;

    @Override
    @CoreWriteTransactional
    public Output execute(Long input) {

        SchedulerConfig config = schedulerConfigRepository.findById(input)
                                                          .orElseThrow(() -> new ResourceNotFoundException(
                                                              "SchedulerConfig not found with id: " + input));

        // Stop the task before deleting
        scheduler.postProcessBeforeDestruction(schedulerTaskCommand, config.getName());

        schedulerConfigRepository.delete(config);

        return new Output(true);
    }

}
