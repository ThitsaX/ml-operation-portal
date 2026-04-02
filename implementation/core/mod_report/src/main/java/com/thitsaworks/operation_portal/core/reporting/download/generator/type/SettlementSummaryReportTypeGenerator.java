package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
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
class SettlementSummaryReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementSummaryReportTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;

    private final GenerateSettlementReportCommand generateSettlementReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {

        return ReportType.SETTLEMENT_SUMMARY;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String settlementId = this.reportGeneratorSupport.requireParam(params, "settlementId");
        String fspId = this.reportGeneratorSupport.requireParam(params, "fspId");
        String dfspName = params.getOrDefault("fspName", fspId);
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String userName = params.getOrDefault("userName", "");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementReportCommand.countRows(
            new GenerateSettlementReportCommand.CountInput(fspId, settlementId));

        LOGGER.info("Total Settlement Summary Row Count : [{}]", totalRowCount);

        if (totalRowCount <= pageSize) {
            GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.execute(
                new GenerateSettlementReportCommand.Input(
                    fspId, dfspName, settlementId, fileType, timezoneOffset, userName, 0, pageSize));

            return new ReportGeneratedFile(output.settlementRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(
                settlementId, fspId, dfspName, fileType, timezoneOffset, userName, totalRowCount, pageSize);
        }

        GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.exportAll(
            new GenerateSettlementReportCommand.Input(
                fspId, dfspName, settlementId, fileType, timezoneOffset, userName, 0, pageSize), totalRowCount, pageSize);

        return new ReportGeneratedFile(output.settlementRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(String settlementId,
                                                 String fspId,
                                                 String dfspName,
                                                 String fileType,
                                                 String timezoneOffset,
                                                 String userName,
                                                 int totalRowCount,
                                                 int pageSize) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateSettlementReportCommand.Output output = this.generateSettlementReportCommand.exportAll(
                    new GenerateSettlementReportCommand.Input(
                        fspId,
                        dfspName,
                        settlementId,
                        fileType,
                        timezoneOffset,
                        userName,
                        offset,
                        rowsInPart), rowsInPart, pageSize);

                zipOutputStream.putNextEntry(
                    new ZipEntry("settlement_summary_part_" + partNumber + "." + fileType));
                zipOutputStream.write(output.settlementRptByte());
                zipOutputStream.closeEntry();

                LOGGER.info(
                    "Generated settlement summary report part [{}] with [{}] rows",
                    partNumber,
                    rowsInPart);
                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }
}
