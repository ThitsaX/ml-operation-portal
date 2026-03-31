package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
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
class SettlementDetailReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementDetailReportTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;

    private final GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

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

        LOGGER.info("Total Settlement Detail Row Count : [{}]", totalRowCount);

        if (totalRowCount <= pageSize) {
            GenerateSettlementDetailReportCommand.Output output =
                this.generateSettlementDetailReportCommand.execute(
                    new GenerateSettlementDetailReportCommand.Input(
                        settlementId,
                        fspId,
                        dfspName,
                        fileType,
                        timezoneOffset,
                        0,
                        Integer.MAX_VALUE));

            return new ReportGeneratedFile(output.settlementDetailRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(
                settlementId,
                fspId,
                dfspName,
                fileType,
                timezoneOffset,
                totalRowCount);
        }

        GenerateSettlementDetailReportCommand.Output output = this.generateSettlementDetailReportCommand.execute(
            new GenerateSettlementDetailReportCommand.Input(
                settlementId,
                fspId,
                dfspName,
                fileType,
                timezoneOffset,
                0,
                Integer.MAX_VALUE));

        return new ReportGeneratedFile(output.settlementDetailRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(String settlementId,
                                                 String fspId,
                                                 String dfspName,
                                                 String fileType,
                                                 String timezoneOffset,
                                                 int totalRowCount) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateSettlementDetailReportCommand.Output output =
                    this.generateSettlementDetailReportCommand.execute(
                        new GenerateSettlementDetailReportCommand.Input(
                            settlementId,
                            fspId,
                            dfspName,
                            fileType,
                            timezoneOffset,
                            offset,
                            rowsInPart));

                String entryName = "settlement_detail_part_" + partNumber + "." + fileType;
                zipOutputStream.putNextEntry(new ZipEntry(entryName));
                zipOutputStream.write(output.settlementDetailRptByte());
                zipOutputStream.closeEntry();

                LOGGER.info(
                    "Generated settlement detail report part [{}] with [{}] rows",
                    partNumber,
                    rowsInPart);

                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }
}
