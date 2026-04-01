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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
class ManagementSummaryReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagementSummaryReportTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;

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
        String userName = params.getOrDefault("userName", "");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateManagementSummaryReportCommand.countRows(new GenerateManagementSummaryReportCommand.CountInput(
                startDate,
                endDate));

        LOGGER.info("Total Row Count : [{}]", totalRowCount);

        GenerateManagementSummaryReportCommand.Input input = new GenerateManagementSummaryReportCommand.Input(startDate,
                                                                                                              endDate,
                                                                                                              timezoneOffset,
                                                                                                              fileType,
                                                                                                              userName,
                                                                                                              0,
                                                                                                              pageSize);
        if (totalRowCount <= pageSize) {
            GenerateManagementSummaryReportCommand.Output output = this.generateManagementSummaryReportCommand.execute(
                input);

            return new ReportGeneratedFile(output.managementSummaryRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(startDate,
                                         endDate,
                                         timezoneOffset,
                                         fileType,
                                         userName,
                                         totalRowCount,
                                         pageSize);
        }

        GenerateManagementSummaryReportCommand.Output output = this.generateManagementSummaryReportCommand.exportAll(
            input,
            totalRowCount,
            pageSize);

        return new ReportGeneratedFile(output.managementSummaryRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(String startDate,
                                                 String endDate,
                                                 String timezoneOffset,
                                                 String fileType,
                                                 String userName,
                                                 int totalRowCount,
                                                 int pageSize) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateManagementSummaryReportCommand.Output
                    partOutput =
                    this.generateManagementSummaryReportCommand.exportAll(new GenerateManagementSummaryReportCommand.Input(
                        startDate,
                        endDate,
                        timezoneOffset,
                        fileType,
                        userName,
                        offset,
                        rowsInPart), rowsInPart, pageSize);

                String entryName = "management_summary_part_" + partNumber + "." + fileType;
                ZipEntry entry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(partOutput.managementSummaryRptByte());
                zipOutputStream.closeEntry();
                LOGGER.info("Generated management summary report part [{}] with [{}] rows", partNumber, rowsInPart);
                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }

}
