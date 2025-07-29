package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.scheduler.data.JobExecutionLogData;
import com.thitsaworks.operation_portal.core.scheduler.query.JobExecutionLogQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetJobExecutionLogs;
import com.thitsaworks.operation_portal.usecase.util.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

@Service
public class GetJobExecutionLogsHandler
    extends OperationPortalAuditableUseCase<GetJobExecutionLogs.Input, GetJobExecutionLogs.Output>
    implements GetJobExecutionLogs {

    private static final Logger LOG = LoggerFactory.getLogger(GetJobExecutionLogsHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(
        UserRoleType.OPERATION,
        UserRoleType.ADMIN
                                                                   );

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final JobExecutionLogQuery jobExecutionLogQuery;

    /**
     * Constructs a new handler with required dependencies.
     */
    public GetJobExecutionLogsHandler(CreateInputAuditCommand createInputAuditCommand,
                                      CreateOutputAuditCommand createOutputAuditCommand,
                                      CreateExceptionAuditCommand createExceptionAuditCommand,
                                      ObjectMapper objectMapper,
                                      PrincipalCache principalCache,
                                      JobExecutionLogQuery jobExecutionLogQuery,
                                      ActionAuthorizationManager actionAuthorizationManager
                                     ) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache,
              actionAuthorizationManager);
        
        this.jobExecutionLogQuery = jobExecutionLogQuery;
    }

    @Override
    public String getName() {

        return "GetJobExecutionLogs";
    }

    @Override
    protected void afterExecute(Output output) throws DomainException {
        // Post-execution handling if needed
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        LOG.info("Fetching job execution logs with filters: jobName={}, status={}, startDate={}, endDate={}",
                 input.jobName()
                      .orElse("<none>"),
                 input.status()
                      .orElse("<none>"),
                 input.startDate()
                      .orElse("<none>"),
                 input.endDate()
                      .orElse("<none>")
                );

        // Create sort object based on input
        Sort sort = Sort.by(
            input.sortDirection()
                 .orElse(Sort.Direction.DESC),
            input.sortBy()
                 .orElse(GetJobExecutionLogs.DEFAULT_SORT_FIELD)
                           );

        // Apply filters based on input
        List<JobExecutionLogData> logs;

        // Case 1: Filter by job name if specified
        if (input.jobName()
                 .isPresent()) {
            logs = jobExecutionLogQuery.getJobExecutionLogsByJobName(
                input.jobName()
                     .get(),
                sort
                                                                    );
        }
        // Case 2: Filter by status if specified
        else if (input.status()
                      .isPresent()) {
            logs = jobExecutionLogQuery.getJobExecutionLogsByStatus(
                input.status()
                     .get(),
                sort
                                                                   );
        }
        // Case 3: Filter by date range if specified
        else if (input.startDate()
                      .isPresent() && input.endDate()
                                           .isPresent()) {
            try {
                LocalDateTime start = LocalDateTime.parse(input.startDate()
                                                               .get(), DATE_FORMAT);
                LocalDateTime end = LocalDateTime.parse(input.endDate()
                                                             .get(), DATE_FORMAT);

                logs = jobExecutionLogQuery.getJobExecutionLogsByDateRange(start, end, sort);
            } catch (DateTimeParseException e) {
                LOG.warn("Invalid date format provided. Expected ISO-8601 format (yyyy-MM-dd'T'HH:mm:ss)");
                throw new IllegalArgumentException(
                    "Invalid date format. Expected ISO-8601 format (e.g., 2023-01-01T00:00:00)");
            }
        }
        // Case 4: No filters, get all logs
        else {
            logs = jobExecutionLogQuery.getJobExecutionLogs(sort);
        }

        return new Output(logs);
    }

}
