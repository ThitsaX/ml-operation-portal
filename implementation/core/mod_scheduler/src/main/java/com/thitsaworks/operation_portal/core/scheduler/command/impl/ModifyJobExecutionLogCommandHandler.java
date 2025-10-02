package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerErrors;
import com.thitsaworks.operation_portal.core.scheduler.exception.SchedulerException;
import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.JobExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyJobExecutionLogCommandHandler implements ModifyJobExecutionLogCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyJobExecutionLogCommandHandler.class);

    private final JobExecutionLogRepository jobExecutionLogRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws DomainException {

        JobExecutionLog jobExecutionLog =
                this.jobExecutionLogRepository.findById(input.jobExecutionLogId())
                                              .orElseThrow(() -> new SchedulerException(SchedulerErrors.JOB_EXECUTION_LOG_NOT_FOUND.format(
                                                      input.jobExecutionLogId()
                                                           .getId()
                                                           .toString())));

        jobExecutionLog.status(input.jobStatus());
        jobExecutionLog.endTime(input.endTime());
        jobExecutionLog.executionMessage(input.executionMessage());

        this.jobExecutionLogRepository.saveAndFlush(jobExecutionLog);

        return new Output(true);

    }

}
