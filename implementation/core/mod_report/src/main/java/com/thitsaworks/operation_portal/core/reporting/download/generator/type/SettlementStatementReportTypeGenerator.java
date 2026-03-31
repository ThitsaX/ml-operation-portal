package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementStatementReportCommand;
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
class SettlementStatementReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementStatementReportTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;

    private final GenerateSettlementStatementReportCommand generateSettlementStatementReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {

        return ReportType.SETTLEMENT_STATEMENT;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String fspId = this.reportGeneratorSupport.normalizeAllToken(this.reportGeneratorSupport.requireParam(params,
                                                                                                               "fspId"));
        String dfspName = params.getOrDefault("dfspName", "");
        Instant startDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "startDate"));
        Instant endDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "endDate"));
        String currencyId = this.reportGeneratorSupport.normalizeAllToken(params.getOrDefault("currencyId", "All"));
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementStatementReportCommand.countRows(
            new GenerateSettlementStatementReportCommand.CountInput(
                fspId,
                startDate,
                endDate,
                currencyId,
                timezoneOffset));

        LOGGER.info("Settlement statement total row count : [{}]", totalRowCount);

        GenerateSettlementStatementReportCommand.Input input = new GenerateSettlementStatementReportCommand.Input(
            fspId,
            dfspName,
            startDate,
            endDate,
            fileType,
            currencyId,
            timezoneOffset,
            0,
            pageSize);

        if (totalRowCount <= pageSize) {
            GenerateSettlementStatementReportCommand.Output output =
                this.generateSettlementStatementReportCommand.execute(input);
            return new ReportGeneratedFile(output.settlementStatementRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(fspId,
                                         dfspName,
                                         startDate,
                                         endDate,
                                         fileType,
                                         currencyId,
                                         timezoneOffset,
                                         totalRowCount,
                                         pageSize);
        }

        GenerateSettlementStatementReportCommand.Output output =
            this.generateSettlementStatementReportCommand.exportAll(input, totalRowCount, pageSize);
        return new ReportGeneratedFile(output.settlementStatementRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(String fspId,
                                                 String dfspName,
                                                 Instant startDate,
                                                 Instant endDate,
                                                 String fileType,
                                                 String currencyId,
                                                 String timezoneOffset,
                                                 int totalRowCount,
                                                 int pageSize) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateSettlementStatementReportCommand.Output partOutput =
                    this.generateSettlementStatementReportCommand.exportAll(
                        new GenerateSettlementStatementReportCommand.Input(
                            fspId,
                            dfspName,
                            startDate,
                            endDate,
                            fileType,
                            currencyId,
                            timezoneOffset,
                            offset,
                            rowsInPart),
                        rowsInPart,
                        pageSize);

                String entryName = "settlement_statement_part_" + partNumber + "." + fileType;
                ZipEntry entry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(partOutput.settlementStatementRptByte());
                zipOutputStream.closeEntry();

                LOGGER.info("Generated settlement statement part [{}] with [{}] rows", partNumber, rowsInPart);
                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }

}
