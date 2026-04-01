package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
class SettlementAuditReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementAuditReportTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;

    private final GenerateSettlementAuditReportCommand generateSettlementAuditReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {

        return ReportType.SETTLEMENT_AUDIT;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        Instant startDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "startDate"));
        Instant endDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "endDate"));
        String dfspId = this.reportGeneratorSupport.normalizeAllToken(params.getOrDefault("dfspId", "All"));
        String dfspName = params.getOrDefault("dfspName", "");
        String currencyId = this.reportGeneratorSupport.normalizeAllToken(params.getOrDefault("currencyId", "All"));
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementAuditReportCommand.countRows(
            new GenerateSettlementAuditReportCommand.CountInput(
                startDate,
                endDate,
                dfspId,
                currencyId,
                timezoneOffset));

        LOGGER.info("Settlement audit total row count : [{}]", totalRowCount);

        GenerateSettlementAuditReportCommand.Input input = new GenerateSettlementAuditReportCommand.Input(
            startDate,
            endDate,
            dfspId,
            dfspName,
            currencyId,
            fileType,
            timezoneOffset,
            0,
            pageSize);

        if (totalRowCount <= pageSize) {
            GenerateSettlementAuditReportCommand.Output output =
                this.generateSettlementAuditReportCommand.execute(input);
            return new ReportGeneratedFile(output.settlementAuditRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(startDate,
                                         endDate,
                                         dfspId,
                                         dfspName,
                                         currencyId,
                                         fileType,
                                         timezoneOffset,
                                         totalRowCount,
                                         pageSize);
        }

        GenerateSettlementAuditReportCommand.Output output =
            this.generateSettlementAuditReportCommand.exportAll(input, totalRowCount, pageSize);
        return new ReportGeneratedFile(output.settlementAuditRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(Instant startDate,
                                                 Instant endDate,
                                                 String dfspId,
                                                 String dfspName,
                                                 String currencyId,
                                                 String fileType,
                                                 String timezoneOffset,
                                                 int totalRowCount,
                                                 int pageSize) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateSettlementAuditReportCommand.Output partOutput =
                    this.generateSettlementAuditReportCommand.exportAll(
                        new GenerateSettlementAuditReportCommand.Input(
                            startDate,
                            endDate,
                            dfspId,
                            dfspName,
                            currencyId,
                            fileType,
                            timezoneOffset,
                            offset,
                            rowsInPart),
                        rowsInPart,
                        pageSize);

                String entryName = "settlement_audit_part_" + partNumber + "." + fileType;
                ZipEntry entry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(partOutput.settlementAuditRptByte());
                zipOutputStream.closeEntry();

                LOGGER.info("Generated settlement audit part [{}] with [{}] rows", partNumber, rowsInPart);
                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }

}
