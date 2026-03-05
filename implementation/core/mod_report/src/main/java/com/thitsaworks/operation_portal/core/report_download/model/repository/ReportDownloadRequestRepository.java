package com.thitsaworks.operation_portal.core.report_download.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.core.report_download.model.ReportDownloadRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportDownloadRequestRepository extends JpaRepository<ReportDownloadRequest, ReportDownloadRequestId>,
                                                         QuerydslPredicateExecutor<ReportDownloadRequest> {

    Optional<ReportDownloadRequest> findTopByStatusOrderByCreatedAtAsc(String status);

    Optional<ReportDownloadRequest> findTopByReportTypeAndParamsSignatureAndDataVersionOrderByCreatedAtDesc(
            String reportType,
            String paramsSignature,
            LocalDate dataVersion);
}
