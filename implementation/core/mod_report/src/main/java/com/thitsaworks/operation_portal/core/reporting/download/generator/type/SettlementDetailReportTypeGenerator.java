package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.PagedZipSupport;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
class SettlementDetailReportTypeGenerator implements ReportTypeGenerator {

    private final GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final PagedZipSupport pagedZipSupport;

    private final ReportGenerator.Settings settings;


    @Override
    public ReportType reportType() {

        return ReportType.SETTLEMENT_DETAIL;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String settlementId = this.reportGeneratorSupport.requireParam(params, "settlementId");
        String fspId = this.reportGeneratorSupport.requireParam(params, "fspId");
        String dfspName = params.getOrDefault("dfspName", fspId);
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementDetailReportCommand.countRows(
            new GenerateSettlementDetailReportCommand.CountInput(settlementId, fspId));

        if (totalRowCount <= pageSize) {
            GenerateSettlementDetailReportCommand.Output output = this.generateSettlementDetailReportCommand.execute(
                new GenerateSettlementDetailReportCommand.Input(
                    settlementId, fspId, dfspName, fileType, timezoneOffset, 0, pageSize));

            return new ReportGeneratedFile(output.settlementDetailRptByte(), fileType);
        }

        return this.pagedZipSupport.generatePagedZip(
            "settlement_detail_part_",
            fileType,
            totalRowCount,
            pageSize,
            (offset, limit) -> this.generateSettlementDetailReportCommand.execute(
                new GenerateSettlementDetailReportCommand.Input(
                    settlementId, fspId, dfspName, fileType, timezoneOffset, offset, limit))
                                                                       .settlementDetailRptByte());
    }
}
