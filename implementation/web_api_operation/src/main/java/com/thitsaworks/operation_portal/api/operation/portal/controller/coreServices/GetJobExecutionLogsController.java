package com.thitsaworks.operation_portal.api.operation.portal.controller.coreServices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.scheduler.data.JobExecutionLogData;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetJobExecutionLogList;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/secured/jobExecutionLogs")
@RequiredArgsConstructor
public class GetJobExecutionLogsController {

    private static final Logger LOG = LoggerFactory.getLogger(GetJobExecutionLogsController.class);

    private final GetJobExecutionLogList getJobExecutionLogList;

    @GetMapping
    public ResponseEntity<Response> execute(
        @RequestParam(value = "jobName", required = false) String jobName,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "startDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(value = "endDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
        @RequestParam(value = "sortBy", required = false) String sortBy,
        @RequestParam(value = "sortDirection", required = false) Sort.Direction sortDirection
                                           ) throws DomainException {
        LOG.debug("Fetching job execution logs with jobName={}, status={}, startDate={}, endDate={}, sortBy={}, sortDirection={}",
                  jobName, status, startDate, endDate, sortBy, sortDirection);

        GetJobExecutionLogList.Output output = this.getJobExecutionLogList.execute(
            new GetJobExecutionLogList.Input(
                Optional.ofNullable(jobName),
                Optional.ofNullable(status),
                Optional.ofNullable(startDate).map(LocalDateTime::toString),
                Optional.ofNullable(endDate).map(LocalDateTime::toString),
                Optional.ofNullable(sortBy),
                Optional.ofNullable(sortDirection)
            )
                                                                                  );

        var response = new Response(output.logs());

        LOG.info("Get Job Execution Logs Response: [{}]", response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        @JsonProperty("jobExecutionLogs") List<JobExecutionLogData> jobExecutionLogs
    ) {}
}