package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.reporting.download.query.ReportDownloadRequestQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetReportDownloadStatus;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GetReportDownloadStatusHandler
        extends OperationPortalUseCase<GetReportDownloadStatus.Input, GetReportDownloadStatus.Output>
        implements GetReportDownloadStatus {

    private final ReportDownloadRequestQuery reportDownloadRequestQuery;


    public GetReportDownloadStatusHandler(PrincipalCache principalCache,
                                          ActionAuthorizationManager actionAuthorizationManager,
                                          ReportDownloadRequestQuery reportDownloadRequestQuery) {

        super(principalCache, actionAuthorizationManager);
        this.reportDownloadRequestQuery = reportDownloadRequestQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var request = this.reportDownloadRequestQuery.findById(input.requestId());
        if (request.isEmpty()) {
            return new Output(null, false);
        }

        FileDownloadStatus status = request.get().status();

        return new Output(status, true);
    }
}
