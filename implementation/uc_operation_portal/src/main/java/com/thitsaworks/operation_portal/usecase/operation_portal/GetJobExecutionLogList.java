package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.scheduler.data.JobExecutionLogData;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * Use case for retrieving job execution logs with optional filtering and sorting.
 * <p>
 * This use case supports:
 * <ul>
 *     <li>Filtering by job name</li>
 *     <li>Filtering by status</li>
 *     <li>Filtering by date range</li>
 *     <li>Sorting by any field with configurable direction</li>
 * </ul>
 */
public interface GetJobExecutionLogList
    extends UseCase<GetJobExecutionLogList.Input, GetJobExecutionLogList.Output> {

    /**
     * The default field to sort by when none is specified.
     */
    String DEFAULT_SORT_FIELD = "startTime";

    /**
     * Input parameters for the use case.
     *
     * @param jobName      optional filter for job name
     * @param status       optional filter for job status
     * @param startDate    optional start date for filtering logs (inclusive)
     * @param endDate      optional end date for filtering logs (inclusive)
     * @param sortBy       the field to sort by, defaults to {@value #DEFAULT_SORT_FIELD}
     * @param sortDirection the sort direction, defaults to DESC (newest first)
     */
    record Input(
        Optional<String> jobName,
        Optional<String> status,
        Optional<String> startDate,
        Optional<String> endDate,
        Optional<@NotNull String> sortBy,
        Optional<Sort.Direction> sortDirection
    ) {
        /**
         * Creates a new Input with default values for any null parameters.
         */
        public Input {
            sortBy = sortBy != null ? sortBy : Optional.of(DEFAULT_SORT_FIELD);
            sortDirection = sortDirection != null ? sortDirection : Optional.of(Sort.Direction.DESC);
        }
    }

    /**
     * Output containing the list of job execution logs.
     *
     * @param logs the list of job execution logs
     */
    record Output(
        @NotNull List<@NotNull JobExecutionLogData> logs
    ) {
        /**
         * Creates a new Output with the provided values.
         *
         * @throws IllegalArgumentException if logs is null or contains null elements
         */
        public Output {
            if (logs == null) {
                throw new IllegalArgumentException("Logs list cannot be null");
            }
            if (logs.stream().anyMatch(log -> log == null)) {
                throw new IllegalArgumentException("Logs list cannot contain null elements");
            }
        }
    }
}
