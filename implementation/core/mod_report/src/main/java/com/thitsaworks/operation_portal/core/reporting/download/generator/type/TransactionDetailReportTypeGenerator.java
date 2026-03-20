package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateTransactionDetailReportCommand;
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
class TransactionDetailReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionDetailReportTypeGenerator.class);
    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;

    private final GenerateTransactionDetailReportCommand generateTransactionDetailReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {

        return ReportType.TRANSACTION_DETAIL;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        Instant startDate = Instant.parse(
            this.reportGeneratorSupport.requireParam(params, "startDate"));
        Instant endDate = Instant.parse(
            this.reportGeneratorSupport.requireParam(params, "endDate"));
        String state = this.reportGeneratorSupport.normalizeAllToken(
            params.getOrDefault("state", "All"));
        String dfspId = this.reportGeneratorSupport.normalizeAllToken(
            params.getOrDefault("dfspId", "All"));
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateTransactionDetailReportCommand.countRows(
            new GenerateTransactionDetailReportCommand.CountInput(
                startDate, endDate, state, dfspId,
                timezoneOffset));

        LOGGER.info("Total Row Count : [{}]", totalRowCount);

        if (totalRowCount <= pageSize) {
            GenerateTransactionDetailReportCommand.Output output = this.generateTransactionDetailReportCommand.execute(
                new GenerateTransactionDetailReportCommand.Input(
                    startDate, endDate, state, dfspId,
                    fileType, timezoneOffset, 0, pageSize));

            return new ReportGeneratedFile(output.transactionDetailRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(
                startDate,
                endDate,
                state,
                dfspId,
                fileType,
                timezoneOffset,
                totalRowCount,
                pageSize);
        }

        GenerateTransactionDetailReportCommand.Output output =
            this.generateTransactionDetailReportCommand.exportAll(
                new GenerateTransactionDetailReportCommand.Input(
                    startDate, endDate, state, dfspId, fileType, timezoneOffset, 0, pageSize),
                totalRowCount,
                pageSize);

        return new ReportGeneratedFile(output.transactionDetailRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(Instant startDate,
                                                 Instant endDate,
                                                 String state,
                                                 String dfspId,
                                                 String fileType,
                                                 String timezoneOffset,
                                                 int totalRowCount,
                                                 int pageSize)
        throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateTransactionDetailReportCommand.Output partOutput =
                    this.generateTransactionDetailReportCommand.exportAll(
                        new GenerateTransactionDetailReportCommand.Input(
                            startDate,
                            endDate,
                            state,
                            dfspId,
                            fileType,
                            timezoneOffset,
                            offset,
                            rowsInPart),
                        rowsInPart,
                        pageSize);

                String entryName = "transaction_detail_part_" + partNumber + "." + fileType;
                ZipEntry entry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(partOutput.transactionDetailRptByte());
                zipOutputStream.closeEntry();
                LOGGER.info("Generated transaction detail report part [{}] with [{}] rows", partNumber, rowsInPart);
                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }

}
