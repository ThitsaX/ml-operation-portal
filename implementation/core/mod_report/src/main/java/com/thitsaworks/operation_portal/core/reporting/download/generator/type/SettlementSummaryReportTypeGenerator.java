package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.PagedZipSupport;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
class SettlementSummaryReportTypeGenerator implements ReportTypeGenerator {

    private final GenerateSettlementReportCommand generateSettlementReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final PagedZipSupport pagedZipSupport;

    private final ReportGenerator.Settings settings;


    @Override
    public ReportType reportType() {

        return ReportType.SETTLEMENT_SUMMARY;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String fspId = this.reportGeneratorSupport.normalizeAllToken(
            this.reportGeneratorSupport.requireParam(params, "fspId"));
        String fspName = params.getOrDefault("fspName", fspId);
        String settlementId = this.reportGeneratorSupport.requireParam(params, "settlementId");
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String userName = params.getOrDefault("userName", "System");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementReportCommand.countRows(
            new GenerateSettlementReportCommand.CountInput(fspId, settlementId));

        if (totalRowCount <= pageSize) {
            GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.execute(
                new GenerateSettlementReportCommand.Input(
                    fspId, fspName, settlementId, fileType,
                    timezoneOffset, userName, 0, pageSize));

            return new ReportGeneratedFile(output.settlementRptByte(), fileType);
        }

        return this.pagedZipSupport.generatePagedZip(
            "settlement_summary_part_", fileType, totalRowCount, pageSize,
            (offset, limit) -> this.generateSettlementReportCommand
                                   .execute(
                                       new GenerateSettlementReportCommand.Input(
                                           fspId, fspName, settlementId, fileType, timezoneOffset,
                                           userName, offset, limit))
                                   .settlementRptByte());
    }

}

