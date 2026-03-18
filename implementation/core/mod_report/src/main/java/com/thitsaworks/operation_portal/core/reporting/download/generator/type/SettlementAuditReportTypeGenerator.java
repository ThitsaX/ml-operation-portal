package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.PagedZipSupport;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
class SettlementAuditReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        SettlementAuditReportTypeGenerator.class);

    private final GenerateSettlementAuditReportCommand generateSettlementAuditReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final PagedZipSupport pagedZipSupport;

    private final ReportGenerator.Settings settings;


    @Override
    public ReportType reportType() {

        return ReportType.SETTLEMENT_AUDIT;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        Instant startDate = Instant.parse(
            this.reportGeneratorSupport.requireParam(params, "startDate"));
        Instant endDate = Instant.parse(
            this.reportGeneratorSupport.requireParam(params, "endDate"));
        String dfspId = this.reportGeneratorSupport.normalizeAllToken(
            params.getOrDefault("dfspId", "All"));
        String dfspName = params.getOrDefault("dfspName", dfspId);
        String currencyId = this.reportGeneratorSupport
                                .requireParam(params, "currencyId")
                                .toUpperCase(Locale.ROOT);
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementAuditReportCommand.countRows(
            new GenerateSettlementAuditReportCommand.CountInput(
                startDate, endDate, dfspId,
                currencyId, timezoneOffset));

        LOGGER.info("Total Row Count : [{}]", totalRowCount);


        if (totalRowCount <= pageSize) {
            GenerateSettlementAuditReportCommand.Output output = this.generateSettlementAuditReportCommand.execute(
                new GenerateSettlementAuditReportCommand.Input(
                    startDate, endDate, dfspId, dfspName, currencyId, fileType, timezoneOffset, 0,
                    pageSize));

            return new ReportGeneratedFile(output.settlementAuditRptByte(), fileType);
        }

        return this.pagedZipSupport.generatePagedZip(
            "settlement_audit_part_", fileType, totalRowCount, pageSize,
            (offset, limit) -> this.generateSettlementAuditReportCommand
                                   .execute(new GenerateSettlementAuditReportCommand.Input(
                                       startDate, endDate, dfspId, dfspName, currencyId, fileType,
                                       timezoneOffset, offset, limit))
                                   .settlementAuditRptByte());
    }

}

