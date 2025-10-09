package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.common.identifier.SchedulerConfigId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.DeleteSchedulerConfigCommand;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerErrors;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerException;
import com.thitsaworks.operation_portal.core.scheduler.model.SchedulerConfig;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.SchedulerConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteSchedulerConfigCommandHandler implements DeleteSchedulerConfigCommand {

    private final SchedulerConfigRepository schedulerConfigRepository;

    private final ScheduledAnnotationBeanPostProcessor scheduler;


    @Override
    @CoreWriteTransactional
    public Output execute(SchedulerConfigId schedulerConfigId) throws DomainException {

        SchedulerConfig config = schedulerConfigRepository.findById(schedulerConfigId)
                                                          .orElseThrow(() -> new SchedulerException(SchedulerErrors.SCHEDULER_CONFIG_NOT_FOUND.format(
                                                                  schedulerConfigId.getId())));

        schedulerConfigRepository.delete(config);

        return new Output(true);
    }

}
