package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.PagedZipSupport;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementStatementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
class SettlementStatementReportTypeGenerator implements ReportTypeGenerator {

    private final GenerateSettlementStatementReportCommand generateSettlementStatementReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final PagedZipSupport pagedZipSupport;

    private final ReportGenerator.Settings settings;


    @Override
    public ReportType reportType() {

        return ReportType.SETTLEMENT_STATEMENT;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String fspId = this.reportGeneratorSupport.normalizeAllToken(
            this.reportGeneratorSupport.requireParam(params, "fspId"));
        String dfspName = params.getOrDefault("dfspName", fspId);
        Instant startDate = Instant.parse(
            this.reportGeneratorSupport.requireParam(params, "startDate"));
        Instant endDate = Instant.parse(
            this.reportGeneratorSupport.requireParam(params, "endDate"));
        String currencyId = this.reportGeneratorSupport
                                .requireParam(params, "currencyId")
                                .toUpperCase(Locale.ROOT);
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementStatementReportCommand.countRows(
            new GenerateSettlementStatementReportCommand.CountInput(
                fspId, startDate, endDate,
                currencyId, timezoneOffset));

        if (totalRowCount <= pageSize) {
            GenerateSettlementStatementReportCommand.Output output = this.generateSettlementStatementReportCommand.execute(
                new GenerateSettlementStatementReportCommand.Input(
                    fspId, dfspName, startDate, endDate, fileType, currencyId, timezoneOffset, 0,
                    pageSize));

            return new ReportGeneratedFile(output.settlementStatementRptByte(), fileType);
        }

        return this.pagedZipSupport.generatePagedZip(
            "settlement_statement_part_", fileType, totalRowCount, pageSize,
            (offset, limit) -> this.generateSettlementStatementReportCommand
                                   .execute(new GenerateSettlementStatementReportCommand.Input(
                                       fspId, dfspName, startDate, endDate, fileType, currencyId,
                                       timezoneOffset, offset, limit))
                                   .settlementStatementRptByte());
    }

}

