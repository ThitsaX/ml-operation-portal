package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.model.JobExecutionLog;
import com.thitsaworks.operation_portal.core.scheduler.model.repository.JobExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateJobExecutionLogCommandHandler implements CreateJobExecutionLogCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateJobExecutionLogCommandHandler.class);

    private final JobExecutionLogRepository jobExecutionLogRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws DomainException {

        JobExecutionLog jobExecutionLog = new JobExecutionLog(
                input.jobName(),
                input.jobStatus(),
                input.startTime()
        );

        var output = this.jobExecutionLogRepository.saveAndFlush(jobExecutionLog);

        return new Output(true, output.getJobExecutionLogId());
    }

}
