package com.thitsaworks.operation_portal.core.reporting.download.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportDownloadRequestRepository extends JpaRepository<ReportDownloadRequest, ReportDownloadRequestId>,
                                                         QuerydslPredicateExecutor<ReportDownloadRequest> {

    Optional<ReportDownloadRequest> findTopByStatusOrderByCreatedAtAsc(FileDownloadStatus status);

    Optional<ReportDownloadRequest> findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
            ReportType reportType,
            String paramsSignature,
            LocalDate dataVersion);

    long countByStatus(FileDownloadStatus status);

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
