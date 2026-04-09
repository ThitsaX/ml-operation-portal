package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.FileDownloadStatus;
import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.storage.S3FileStorage;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.cache.UserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.reporting.download.request.ReportDownloadRequestManager;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GenerateSettlementBankOverviewReport;
import com.thitsaworks.operation_portal.usecase.util.ReportDownloadUtil;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class GenerateSettlementBankOverviewReportHandler
    extends OperationPortalAuditableUseCase<GenerateSettlementBankOverviewReport.Input, GenerateSettlementBankOverviewReport.Output>
    implements GenerateSettlementBankOverviewReport {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementBankOverviewReportHandler.class);

    private final ReportDownloadRequestManager reportDownloadRequestManager;

    private final ParticipantCache participantCache;

    private final UserCache userCache;

    private final S3FileStorage s3FileStorage;

    public GenerateSettlementBankOverviewReportHandler(CreateInputAuditCommand createInputAuditCommand,
                                                       CreateOutputAuditCommand createOutputAuditCommand,
                                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                                       ObjectMapper objectMapper,
                                                       PrincipalCache principalCache,
                                                       ActionAuthorizationManager actionAuthorizationManager,
                                                       ReportDownloadRequestManager reportDownloadRequestManager,
                                                       ParticipantCache participantCache,
                                                       UserCache userCache,
                                                       S3FileStorage s3FileStorage) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              objectMapper,
              principalCache,
              actionAuthorizationManager);

        this.reportDownloadRequestManager = reportDownloadRequestManager;
        this.participantCache = participantCache;
        this.userCache = userCache;
        this.s3FileStorage = s3FileStorage;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        String normalizedFileType = ReportDownloadUtil.normalizeFileType(input.fileType());
        if (!"xlsx".equals(normalizedFileType) && !"pdf".equals(normalizedFileType)) {
            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
        }

        UserData getUser = this.userCache.get(new UserId(input.userId()));
        if (getUser == null) {
            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND.format(input.userId().toString()));
        }

        ParticipantData userParticipant = this.participantCache.get(getUser.participantId());
        if (userParticipant == null) {
            throw new ParticipantException(
                ParticipantErrors.PARTICIPANT_NOT_FOUND.format(getUser.participantId().getId().toString()));
        }

        boolean isParent = userParticipant.parentParticipantName() == null ||
            userParticipant.parentParticipantName().isBlank();
        String normalizedCurrency = input.currencyId() == null
            ? "ALL"
            : input.currencyId().toUpperCase(Locale.ROOT);

        Map<String, String> params = new HashMap<>();
        params.put("settlementId", input.settlementId());
        params.put("currencyId", normalizedCurrency);
        params.put("timezoneOffset", input.timezone());
        params.put("userName", getUser.name());
        params.put("dfspId", userParticipant.participantName().getValue());
        params.put("isParent", String.valueOf(isParent));

        ReportDownloadRequestManager.CreateOrReuseResult result = this.reportDownloadRequestManager.createPendingOrReuse(
            ReportType.SETTLEMENT_BANK_OVERVIEW,
            normalizedFileType,
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
                    ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION));
        }

        return new Output(
            result.request().requestId(), result.request().status(), fileUrl, fileKey, result.paramsSignature());
    }
}
