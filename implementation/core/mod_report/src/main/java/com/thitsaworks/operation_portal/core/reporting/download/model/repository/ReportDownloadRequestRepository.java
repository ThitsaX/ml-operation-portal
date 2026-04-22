package com.thitsaworks.operation_portal.core.reporting.download.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface ReportDownloadRequestRepository extends JpaRepository<ReportDownloadRequest, ReportDownloadRequestId>,
                                                         QuerydslPredicateExecutor<ReportDownloadRequest> {

    Optional<ReportDownloadRequest> findTopByStatusOrderByCreatedAtAsc(FileDownloadStatus status);

    long countByStatus(FileDownloadStatus status);

    long countByStatusAndUpdatedAtGreaterThanEqual(FileDownloadStatus status, Instant updatedAt);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "UPDATE ReportDownloadRequest r " +
        "SET r.status = :failedStatus, " +
        "r.errorMessage = :errorMessage, " +
        "r.finishedDate = :finishedAt, " +
        "r.updatedAt = :updatedAt " +
        "WHERE r.status = :runningStatus AND r.updatedAt < :staleBefore")
    int failStaleRunningRequests(@Param("runningStatus") FileDownloadStatus runningStatus,
                                 @Param("failedStatus") FileDownloadStatus failedStatus,
                                 @Param("errorMessage") String errorMessage,
                                 @Param("staleBefore") Instant staleBefore,
                                 @Param("finishedAt") Instant finishedAt,
                                 @Param("updatedAt") Instant updatedAt);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "UPDATE ReportDownloadRequest r " +
        "SET r.updatedAt = :updatedAt " +
        "WHERE r.requestId = :requestId AND r.status = :runningStatus")
    int touchRunningRequest(@Param("requestId") ReportDownloadRequestId requestId,
                            @Param("runningStatus") FileDownloadStatus runningStatus,
                            @Param("updatedAt") Instant updatedAt);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "UPDATE ReportDownloadRequest r " +
        "SET r.status = :readyStatus, " +
        "r.fileUrl = :fileUrl, " +
        "r.errorMessage = NULL, " +
        "r.finishedDate = :finishedAt, " +
        "r.updatedAt = :updatedAt " +
        "WHERE r.requestId = :requestId AND r.status = :runningStatus")
    int transitionToReadyIfRunning(@Param("requestId") ReportDownloadRequestId requestId,
                                   @Param("runningStatus") FileDownloadStatus runningStatus,
                                   @Param("readyStatus") FileDownloadStatus readyStatus,
                                   @Param("fileUrl") String fileUrl,
                                   @Param("finishedAt") Instant finishedAt,
                                   @Param("updatedAt") Instant updatedAt);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "UPDATE ReportDownloadRequest r " +
        "SET r.status = :failedStatus, " +
        "r.errorMessage = :errorMessage, " +
        "r.finishedDate = :finishedAt, " +
        "r.updatedAt = :updatedAt " +
        "WHERE r.requestId = :requestId AND r.status = :runningStatus")
    int transitionToFailedIfRunning(@Param("requestId") ReportDownloadRequestId requestId,
                                    @Param("runningStatus") FileDownloadStatus runningStatus,
                                    @Param("failedStatus") FileDownloadStatus failedStatus,
                                    @Param("errorMessage") String errorMessage,
                                    @Param("finishedAt") Instant finishedAt,
                                    @Param("updatedAt") Instant updatedAt);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
        "UPDATE ReportDownloadRequest r " +
        "SET r.status = :toStatus, r.updatedAt = :updatedAt " +
        "WHERE r.requestId = :requestId AND r.status = :fromStatus")
    int updateStatusIfCurrent(@Param("requestId") ReportDownloadRequestId requestId,
                              @Param("fromStatus") FileDownloadStatus fromStatus,
                              @Param("toStatus") FileDownloadStatus toStatus,
                              @Param("updatedAt") Instant updatedAt);
}
