package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateOrUpdateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.InitializeScheduledTasksCommand;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitializeScheduledTasksCommandHandler implements InitializeScheduledTasksCommand {

    private final SchedulerConfigRepository schedulerConfigRepository;

    private final CreateOrUpdateSchedulerConfigCommand createOrUpdateSchedulerConfigCommand;

    @Override
    @CoreWriteTransactional
    public Output execute() {

        schedulerConfigRepository.findByActiveTrue()
                                 .forEach(config ->
                                              createOrUpdateSchedulerConfigCommand.rescheduleTask(config.getName(),
                                                                                                  config.getCronExpression())
                                         );

        return new Output(true);
    }

}
