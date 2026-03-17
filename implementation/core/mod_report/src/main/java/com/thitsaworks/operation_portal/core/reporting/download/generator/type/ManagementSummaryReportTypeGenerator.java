package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateManagementSummaryReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
class ManagementSummaryReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementSummaryReportTypeGenerator.class);

    private final GenerateManagementSummaryReportCommand generateManagementSummaryReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {

        return ReportType.MANAGEMENT_SUMMARY;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String startDate = this.reportGeneratorSupport.requireParam(params, "startDate");
        String endDate = this.reportGeneratorSupport.requireParam(params, "endDate");
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String userName = params.getOrDefault("userName", "System");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateManagementSummaryReportCommand.countRows(
            new GenerateManagementSummaryReportCommand.CountInput(startDate, endDate));

        LOGGER.info("Total Row Count : [{}]", totalRowCount);

        if (totalRowCount <= pageSize) {
            GenerateManagementSummaryReportCommand.Output output = this.generateManagementSummaryReportCommand.execute(
                new GenerateManagementSummaryReportCommand.Input(
                    startDate, endDate, timezoneOffset,
                    fileType, userName, 0, pageSize));

            return new ReportGeneratedFile(output.managementSummaryRptByte(), fileType);
        }

        GenerateManagementSummaryReportCommand.Output output =
            this.generateManagementSummaryReportCommand.exportAll(
                new GenerateManagementSummaryReportCommand.Input(
                    startDate, endDate, timezoneOffset, fileType, userName, 0, pageSize),
                totalRowCount,
                pageSize);

        return new ReportGeneratedFile(output.managementSummaryRptByte(), fileType);
    }

}
