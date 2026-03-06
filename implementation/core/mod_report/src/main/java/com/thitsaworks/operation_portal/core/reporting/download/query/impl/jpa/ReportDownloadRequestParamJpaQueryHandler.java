package com.thitsaworks.operation_portal.core.reporting.download.query.impl.jpa;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.reporting.download.data.ReportDownloadRequestParamData;
import com.thitsaworks.operation_portal.core.reporting.download.model.repository.ReportDownloadRequestParamRepository;
import com.thitsaworks.operation_portal.core.reporting.download.query.ReportDownloadRequestParamQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class ReportDownloadRequestParamJpaQueryHandler implements ReportDownloadRequestParamQuery {

    private final ReportDownloadRequestParamRepository reportDownloadRequestParamRepository;

    @Override
    public List<ReportDownloadRequestParamData> findByRequestId(ReportDownloadRequestId requestId) {

        return this.reportDownloadRequestParamRepository
                   .findByRequestId(requestId)
                   .stream()
                   .map(ReportDownloadRequestParamData::new)
                   .toList();
    }
}
