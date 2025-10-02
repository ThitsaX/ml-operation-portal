package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerErrors;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerException;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateSchedulerConfigCommandHandler implements CreateSchedulerConfigCommand {

    private final SchedulerConfigRepository schedulerConfigRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws DomainException {

        Optional<SchedulerConfig> optionalSchedulerConfig = schedulerConfigRepository.findByName(input.name());

        if (optionalSchedulerConfig.isPresent()) {
            throw new SchedulerException(SchedulerErrors.SCHEDULER_ALREADY_REGISTERED.format(input.name()));
        }

        SchedulerConfig schedulerConfig = new SchedulerConfig(input.name(),
                                                              input.jobName(),
                                                              input.cronExpression(),
                                                              input.description());

        this.schedulerConfigRepository.save(schedulerConfig);

        return new Output(new SchedulerConfigData(schedulerConfig));
    }

}
