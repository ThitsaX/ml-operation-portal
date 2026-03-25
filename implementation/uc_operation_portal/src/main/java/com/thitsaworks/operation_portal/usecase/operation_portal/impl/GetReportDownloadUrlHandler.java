package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.storage.S3FileStorage;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.reporting.download.query.ReportDownloadRequestQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetReportDownloadUrl;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
public class GetReportDownloadUrlHandler
    extends OperationPortalAuditableUseCase<GetReportDownloadUrl.Input, GetReportDownloadUrl.Output>
    implements GetReportDownloadUrl {

    private static final Logger LOG = LoggerFactory.getLogger(GetReportDownloadUrlHandler.class);

    private final ReportDownloadRequestQuery reportDownloadRequestQuery;

    private final S3FileStorage s3FileStorage;

    public GetReportDownloadUrlHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       ActionAuthorizationManager actionAuthorizationManager,
                                       ReportDownloadRequestQuery reportDownloadRequestQuery,
                                       S3FileStorage s3FileStorage) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.reportDownloadRequestQuery = reportDownloadRequestQuery;
        this.s3FileStorage = s3FileStorage;
    }

    @Override
    public String getName() {

        return "GetReportDownloadStatus";
    }

    @Override
    protected Output onExecute(Input input)
        throws DomainException, ConnectException, JsonProcessingException {

        var request = this.reportDownloadRequestQuery.findById(input.requestId());

        if (request.isEmpty()) {
            return new Output(null, null, null, false);
        }

        FileDownloadStatus status = request.get().status();
        String fileKey = request.get().fileUrl();
        String fileUrl = null;

        if (FileDownloadStatus.READY.equals(status) && fileKey != null && !fileKey.isBlank()) {
            try {
                fileUrl = this.s3FileStorage.generatePreSignedDownloadUrl(fileKey);
            } catch (Exception exception) {
                LOG.warn("Failed to generate pre-signed URL for requestId [{}]: [{}]",
                         request.get().requestId().getEntityId(),
                         exception.getMessage());
            }
        }

        return new Output(status, fileUrl, fileKey, true);
    }
}
