package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
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
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class GenerateTransactionDetailReportHandler
    extends OperationPortalAuditableUseCase<GenerateTransactionDetailReport.Input, GenerateTransactionDetailReport.Output>
    implements GenerateTransactionDetailReport {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateTransactionDetailReportHandler.class);

    private static final String REPORT_TYPE_TRANSACTION_DETAIL = "TRANSACTION_DETAIL";

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
        params.put("state", normalizeAllToken(input.state()));
        params.put("dfspId", normalizeAllToken(input.dfspId()));
        params.put("timezoneOffset", input.timezone());

        ReportDownloadRequestManager.CreateOrReuseResult result = this.reportDownloadRequestManager.createPendingOrReuse(
            ReportType.valueOf(REPORT_TYPE_TRANSACTION_DETAIL), normalizeFileType(input.fileType()),
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
            
            throw new ReportException(this.resolveFailedError(result.request().errorMessage()));
        }

        return new Output(
            result.request().requestId(), result.request().status(), fileUrl, fileKey,
            result.reused(), result.paramsSignature());
    }

    private String normalizeAllToken(String value) {

        if (value == null) {

            return "All";
        }

        return "all".equalsIgnoreCase(value.trim()) ? "All" : value.trim();
    }

    private String normalizeFileType(String fileType) {

        return fileType == null ? "" : fileType.trim().toLowerCase(Locale.ROOT);
    }

    private ErrorMessage resolveFailedError(String storedError) {

        if (storedError == null || storedError.isBlank()) {

            return ReportErrors.TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION;
        }

        int delimiterIndex = storedError.indexOf("-");
        String errorCode = delimiterIndex > 0 ? storedError.substring(0, delimiterIndex) : storedError;
        String errorDefaultMessage = delimiterIndex > 0 &&
            storedError.length() > delimiterIndex + 1 ? storedError.substring(delimiterIndex + 1) : "";

        return switch (errorCode) {
            case "RESULT_NOT_FOUND_EXCEPTION" -> this.withDefaultMessage(
                ReportErrors.RESULT_NOT_FOUND_EXCEPTION, errorDefaultMessage);
            case "FILE_FORMAT_NOT_ALLOWED_EXCEPTION" -> this.withDefaultMessage(
                ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION, errorDefaultMessage);
            case "REPORT_MAXIMUM_LIMIT_EXCEPTION" -> this.withDefaultMessage(
                ReportErrors.REPORT_MAXIMUM_LIMIT_EXCEPTION, errorDefaultMessage);
            case "TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION" -> this.withDefaultMessage(
                ReportErrors.TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION, errorDefaultMessage);
            default -> ReportErrors.TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION.defaultMessage(storedError);
        };
    }

    private ErrorMessage withDefaultMessage(ErrorMessage errorMessage, String defaultMessage) {

        if (defaultMessage == null || defaultMessage.isBlank()) {

            return errorMessage;
        }

        return errorMessage.defaultMessage(defaultMessage);
    }

}
