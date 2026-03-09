package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.storage.S3FileStorage;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.reporting.download.request.ReportDownloadRequestManager;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateTransactionDetailReport;
import com.thitsaworks.operation_portal.usecase.util.ReportDownloadUtil;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateTransactionDetailReportHandler
    extends OperationPortalAuditableUseCase<GenerateTransactionDetailReport.Input, GenerateTransactionDetailReport.Output>
    implements GenerateTransactionDetailReport {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateTransactionDetailReportHandler.class);

    private final ReportDownloadRequestManager reportDownloadRequestManager;

    private final S3FileStorage s3FileStorage;

    public GenerateTransactionDetailReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                  CreateOutputAuditCommand createOutputAuditCommand,
                                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                                  ObjectMapper objectMapper,
                                                  PrincipalCache principalCache,
                                                  ActionAuthorizationManager actionAuthorizationManager,
                                                  ReportDownloadRequestManager reportDownloadRequestManager,
                                                  S3FileStorage s3FileStorage) {

        super(
            createInputAuditCommand, createOutputAuditCommand, createExceptionAuditCommand,
            objectMapper, principalCache, actionAuthorizationManager);

        this.reportDownloadRequestManager = reportDownloadRequestManager;
        this.s3FileStorage = s3FileStorage;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        Map<String, String> params = new HashMap<>();
        params.put("startDate", input.startDate().toString());
        params.put("endDate", input.endDate().toString());
        params.put("state", ReportDownloadUtil.normalizeAllToken(input.state()));
        params.put("dfspId", ReportDownloadUtil.normalizeAllToken(input.dfspId()));
        params.put("timezoneOffset", input.timezone());

        ReportDownloadRequestManager.CreateOrReuseResult result = this.reportDownloadRequestManager.createPendingOrReuse(
            ReportType.TRANSACTION_DETAIL, ReportDownloadUtil.normalizeFileType(input.fileType()),
            null, params);

        String fileKey = result.request().fileUrl();
        String fileUrl = null;

        if (FileDownloadStatus.READY.equals(result.request().status()) && fileKey != null &&
                !fileKey.isBlank()) {

            try {

                fileUrl = this.s3FileStorage.generatePreSignedDownloadUrl(fileKey);

            } catch (Exception e) {

                LOG.warn(
                    "Failed to generate pre-signed URL for requestId [{}]: [{}]",
                    result.request().requestId().getEntityId(), e.getMessage());
            }
        }

        if (FileDownloadStatus.FAILED.equals(result.request().status())) {

            throw new ReportException(
                ReportDownloadUtil.resolveFailedError(
                    result.request().errorMessage(),
                    ReportErrors.TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION));
        }

        return new Output(
            result.request().requestId(), result.request().status(), fileUrl, fileKey,
            result.reused(), result.paramsSignature());
    }
}
