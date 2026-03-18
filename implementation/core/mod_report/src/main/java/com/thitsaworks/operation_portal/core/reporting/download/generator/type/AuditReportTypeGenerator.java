package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.PagedZipSupport;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class AuditReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditReportTypeGenerator.class);

    private final GenerateAuditReportCommand generateAuditReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final PagedZipSupport pagedZipSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {

        return ReportType.AUDIT;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        Instant fromDate = Instant.parse(
            this.reportGeneratorSupport.requireParam(params, "fromDate"));
        Instant toDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "toDate"));
        String realmId = this.reportGeneratorSupport.normalizeOptionalFilter(params.get("realmId"));
        String userId = this.reportGeneratorSupport.normalizeOptionalFilter(params.get("userId"));
        String actionId = this.reportGeneratorSupport.normalizeOptionalFilter(
            params.get("actionId"));
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());
        List<String> grantedActionList = this.reportGeneratorSupport.parseListParam(
            params.get("grantedActionList"));

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateAuditReportCommand.countRows(
            new GenerateAuditReportCommand.CountInput(
                realmId, fromDate, toDate, userId, actionId,
                grantedActionList));

        LOGGER.info("Total Row Count : [{}]", totalRowCount);

        if (totalRowCount <= pageSize) {
            GenerateAuditReportCommand.Output output = this.generateAuditReportCommand.execute(
                new GenerateAuditReportCommand.Input(
                    realmId, fromDate, toDate, timezoneOffset, userId, actionId, fileType,
                    grantedActionList, 0, pageSize));

            return new ReportGeneratedFile(output.auditRptByte(), fileType);
        }

        return this.pagedZipSupport.generatePagedZip(
            "audit_part_", fileType, totalRowCount, pageSize,
            (offset, limit) -> this.generateAuditReportCommand
                                   .execute(new GenerateAuditReportCommand.Input(
                                       realmId, fromDate, toDate, timezoneOffset, userId, actionId,
                                       fileType, grantedActionList, offset, limit))
                                   .auditRptByte());
    }

}

