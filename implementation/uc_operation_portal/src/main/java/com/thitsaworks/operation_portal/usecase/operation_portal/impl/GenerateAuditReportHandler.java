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
import com.thitsaworks.operation_portal.core.iam.query.IAMQuery;
import com.thitsaworks.operation_portal.core.reporting.download.request.ReportDownloadRequestManager;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateAuditReport;
import com.thitsaworks.operation_portal.usecase.util.ReportDownloadUtil;
import com.thitsaworks.operation_portal.usecase.util.UserPermissionManager;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenerateAuditReportHandler
    extends OperationPortalAuditableUseCase<GenerateAuditReport.Input, GenerateAuditReport.Output>
    implements GenerateAuditReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateAuditReportHandler.class);

    private final IAMQuery iamQuery;

    private final UserPermissionManager userPermissionManager;

    private final ReportDownloadRequestManager reportDownloadRequestManager;

    private final S3FileStorage s3FileStorage;

    public GenerateAuditReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                      CreateOutputAuditCommand createOutputAuditCommand,
                                      CreateExceptionAuditCommand createExceptionAuditCommand,
                                      ObjectMapper objectMapper,
                                      PrincipalCache principalCache,
                                      ActionAuthorizationManager actionAuthorizationManager,
                                      IAMQuery iamQuery,
                                      UserPermissionManager userPermissionManager,
                                      ReportDownloadRequestManager reportDownloadRequestManager,
                                      S3FileStorage s3FileStorage) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.iamQuery = iamQuery;
        this.userPermissionManager = userPermissionManager;
        this.reportDownloadRequestManager = reportDownloadRequestManager;
        this.s3FileStorage = s3FileStorage;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {

        var currentUser = this.userPermissionManager.getCurrentUser();

        String realmId = null;
        if (this.userPermissionManager.isDfsp(currentUser.principalId())) {
            realmId = currentUser.realmId().toString();
        }

        String userId = input.userId() == null ? null : input.userId().getEntityId().toString();
        String actionId = input.actionId() == null ? null : input.actionId().getEntityId().toString();

        List<String> grantedActionList = this.iamQuery.getGrantedActionsByPrincipal(currentUser.principalId())
                                                      .stream()
                                                      .map(action -> String.valueOf(action.actionId().getEntityId()))
                                                      .sorted()
                                                      .toList();

        Map<String, String> params = new HashMap<>();
        params.put("fromDate", input.fromDate().toString());
        params.put("toDate", input.toDate().toString());
        params.put("timezoneOffset", input.timezoneOffset());
        params.put("realmId", ReportDownloadUtil.normalizeAllToken(realmId));
        params.put("userId", ReportDownloadUtil.normalizeAllToken(userId));
        params.put("actionId", ReportDownloadUtil.normalizeAllToken(actionId));
        params.put("grantedActionList", grantedActionList.stream().collect(Collectors.joining(",")));

        ReportDownloadRequestManager.CreateOrReuseResult result = this.reportDownloadRequestManager.createPendingOrReuse(
            ReportType.AUDIT,
            ReportDownloadUtil.normalizeFileType(input.fileType()),
            null,
            params);

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
                    ReportErrors.AUDIT_REPORT_FAILURE_EXCEPTION));
        }

        return new Output(
            result.request().requestId(), result.request().status(), fileUrl, fileKey,
            result.reused(), result.paramsSignature());
    }

}
