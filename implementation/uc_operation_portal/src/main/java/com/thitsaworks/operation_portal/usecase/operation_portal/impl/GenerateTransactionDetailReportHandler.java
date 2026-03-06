package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.report_download.model.repository.ReportDownloadRequestRepository;
import com.thitsaworks.operation_portal.core.report_download.request.ReportDownloadRequestManager;
import com.thitsaworks.operation_portal.core.report_download.storage.ReportS3Storage;
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

    private static final Logger LOG = LoggerFactory.getLogger(GenerateTransactionDetailReportHandler.class);

    private static final String REPORT_TYPE_TRANSACTION_DETAIL = "TRANSACTION_DETAIL";
    private static final String STATUS_READY = "READY";
    private final ReportDownloadRequestManager reportDownloadRequestManager;
    private final ReportDownloadRequestRepository reportDownloadRequestRepository;
    private final ReportS3Storage reportS3Storage;

    public GenerateTransactionDetailReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                  CreateOutputAuditCommand createOutputAuditCommand,
                                                  CreateExceptionAuditCommand createExceptionAuditCommand,
                                                  ObjectMapper objectMapper,
                                                  PrincipalCache principalCache,
                                                  ActionAuthorizationManager actionAuthorizationManager,
                                                  ReportDownloadRequestManager reportDownloadRequestManager,
                                                  ReportDownloadRequestRepository reportDownloadRequestRepository,
                                                  ReportS3Storage reportS3Storage) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.reportDownloadRequestManager = reportDownloadRequestManager;
        this.reportDownloadRequestRepository = reportDownloadRequestRepository;
        this.reportS3Storage = reportS3Storage;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        Map<String, String> params = new HashMap<>();
        params.put("startDate", input.startDate().toString());
        params.put("endDate", input.endDate().toString());
        params.put("state", normalizeAllToken(input.state()));
        params.put("dfspId", normalizeAllToken(input.dfspId()));
        params.put("timezoneOffset", input.timezone());

        ReportDownloadRequestManager.CreateOrReuseResult result =
                this.reportDownloadRequestManager.createPendingOrReuse(
                        REPORT_TYPE_TRANSACTION_DETAIL,
                        normalizeFileType(input.fileType()),
                        null,
                        params);

        String fileUrl = result.request().getFileUrl();

        if (result.reused()
            && STATUS_READY.equalsIgnoreCase(result.request().getStatus())
            && fileUrl != null
            && !fileUrl.isBlank()) {

            try {

                fileUrl = this.reportS3Storage.regeneratePreSignedDownloadUrl(fileUrl);
                result.request().fileUrl(fileUrl);
                result.request().updatedDate(java.time.Instant.now());
                this.reportDownloadRequestRepository.save(result.request());

            } catch (Exception e) {

                LOG.warn("Failed to regenerate pre-signed URL for requestId [{}]: [{}]",
                         result.request().getId().getEntityId(),
                         e.getMessage());
            }
        }

        return new Output(result.request().getId().getEntityId(),
                          result.request().getStatus(),
                          fileUrl,
                          result.reused(),
                          result.paramsSignature());
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

}
