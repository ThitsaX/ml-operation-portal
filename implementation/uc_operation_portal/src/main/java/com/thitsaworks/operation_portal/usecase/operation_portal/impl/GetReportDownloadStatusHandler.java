package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.identifier.ReportDownloadRequestId;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.report_download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.core.report_download.model.repository.ReportDownloadRequestRepository;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetReportDownloadStatus;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Optional;

@Service
public class GetReportDownloadStatusHandler
        extends OperationPortalUseCase<GetReportDownloadStatus.Input, GetReportDownloadStatus.Output>
        implements GetReportDownloadStatus {

    private final ReportDownloadRequestRepository reportDownloadRequestRepository;

    public GetReportDownloadStatusHandler(PrincipalCache principalCache,
                                          ActionAuthorizationManager actionAuthorizationManager,
                                          ReportDownloadRequestRepository reportDownloadRequestRepository) {

        super(principalCache, actionAuthorizationManager);
        this.reportDownloadRequestRepository = reportDownloadRequestRepository;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        Optional<ReportDownloadRequest> requestOptional =
                this.reportDownloadRequestRepository.findById(new ReportDownloadRequestId(input.requestId()));

        if (requestOptional.isEmpty()) {

            return new Output("NOT_FOUND", false);
        }

        ReportDownloadRequest request = requestOptional.get();
        return new Output(request.getStatus(), true);
    }
}
