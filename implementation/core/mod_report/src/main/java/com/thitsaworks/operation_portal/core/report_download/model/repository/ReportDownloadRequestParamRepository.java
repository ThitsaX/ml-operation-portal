package com.thitsaworks.operation_portal.core.report_download.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestParamId;
import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.core.report_download.model.ReportDownloadRequestParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportDownloadRequestParamRepository extends JpaRepository<ReportDownloadRequestParam, ReportDownloadRequestParamId>,
                                                              QuerydslPredicateExecutor<ReportDownloadRequestParam> {

    List<ReportDownloadRequestParam> findByRequestId(ReportDownloadRequestId requestId);
}
