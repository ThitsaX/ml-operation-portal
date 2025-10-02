package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifySchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerErrors;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerException;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifySchedulerConfigCommandHandler implements ModifySchedulerConfigCommand {

    private final SchedulerConfigRepository schedulerConfigRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws DomainException {

        SchedulerConfig schedulerConfig = this.schedulerConfigRepository.findById(input.schedulerConfigId())
                                                                        .orElseThrow(() -> new SchedulerException(
                                                                                SchedulerErrors.SCHEDULER_CONFIG_NOT_FOUND.format(
                                                                                        input.schedulerConfigId()
                                                                                             .getId())));

        this.schedulerConfigRepository.save(schedulerConfig.name(input.name())
                                                           .jobName(input.jobName())
                                                           .cronExpression(input.cronExpression())
                                                           .description(input.description())
                                                           .active(input.active()));

        return new Output(new SchedulerConfigData(schedulerConfig));
    }

}
