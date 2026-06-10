package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
<<<<<<< Updated upstream
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateTransactionDetailReportCommand;
=======
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSummaryReportCommand;
>>>>>>> Stashed changes
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
class FeeSummaryReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeeSummaryReportTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("ddMMMyyyy");

<<<<<<< Updated upstream
    private final GenerateTransactionDetailReportCommand generateTransactionDetailReportCommand;
=======
    private final GenerateFeeSummaryReportCommand generateFeeSummaryReportCommand;
>>>>>>> Stashed changes

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {

<<<<<<< Updated upstream
        return ReportType.TransactionDetail;
=======
        return ReportType.FEE_SUMMARY;
>>>>>>> Stashed changes
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        Instant startDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "startDate"));
        Instant endDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "endDate"));
<<<<<<< Updated upstream
        String state = this.reportGeneratorSupport.normalizeAllToken(params.getOrDefault("state", "All"));
=======
>>>>>>> Stashed changes
        String dfspId = this.reportGeneratorSupport.normalizeAllToken(params.getOrDefault("dfspId", "All"));
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
<<<<<<< Updated upstream
        int totalRowCount = this.generateTransactionDetailReportCommand.countRows(new GenerateTransactionDetailReportCommand.CountInput(
                startDate,
                endDate,
                state,
=======
        int totalRowCount = this.generateFeeSummaryReportCommand.countRows(new GenerateFeeSummaryReportCommand.CountInput(
                startDate,
                endDate,
>>>>>>> Stashed changes
                dfspId,
                timezoneOffset));

        LOGGER.info("Total Row Count : [{}]", totalRowCount);

<<<<<<< Updated upstream
        GenerateTransactionDetailReportCommand.Input input = new GenerateTransactionDetailReportCommand.Input(startDate,
                                                                                                              endDate,
                                                                                                              state,
                                                                                                              dfspId,
                                                                                                              fileType,
                                                                                                              timezoneOffset,
                                                                                                              0,
                                                                                                              pageSize);
        if (totalRowCount <= pageSize) {
            GenerateTransactionDetailReportCommand.Output output = this.generateTransactionDetailReportCommand.execute(
                input);

            return new ReportGeneratedFile(output.transactionDetailRptByte(), fileType);
=======
        GenerateFeeSummaryReportCommand.Input input = new GenerateFeeSummaryReportCommand.Input(startDate,
                                                                                                  endDate,
                                                                                                  dfspId,
                                                                                                  fileType,
                                                                                                  timezoneOffset,
                                                                                                  0,
                                                                                                  pageSize);
        if (totalRowCount <= pageSize) {
            GenerateFeeSummaryReportCommand.Output output = this.generateFeeSummaryReportCommand.execute(
                input);

            return new ReportGeneratedFile(output.feeSummaryRptByte(), fileType);
>>>>>>> Stashed changes
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(request,
                                         startDate,
                                         endDate,
<<<<<<< Updated upstream
                                         state,
=======
>>>>>>> Stashed changes
                                         dfspId,
                                         fileType,
                                         timezoneOffset,
                                         totalRowCount,
                                         pageSize);
        }

<<<<<<< Updated upstream
        GenerateTransactionDetailReportCommand.Output output = this.generateTransactionDetailReportCommand.exportAll(
=======
        GenerateFeeSummaryReportCommand.Output output = this.generateFeeSummaryReportCommand.exportAll(
>>>>>>> Stashed changes
            input,
            totalRowCount,
            pageSize);

<<<<<<< Updated upstream
        return new ReportGeneratedFile(output.transactionDetailRptByte(), fileType);
=======
        return new ReportGeneratedFile(output.feeSummaryRptByte(), fileType);
>>>>>>> Stashed changes
    }

    private ReportGeneratedFile generateSplitZip(ReportDownloadRequest request,
                                                 Instant startDate,
                                                 Instant endDate,
<<<<<<< Updated upstream
                                                 String state,
=======
>>>>>>> Stashed changes
                                                 String dfspId,
                                                 String fileType,
                                                 String timezoneOffset,
                                                 int totalRowCount,
                                                 int pageSize) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            String baseFileName = this.baseFileName(request);
            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

<<<<<<< Updated upstream
                GenerateTransactionDetailReportCommand.Output
                    partOutput =
                    this.generateTransactionDetailReportCommand.exportAll(new GenerateTransactionDetailReportCommand.Input(
                        startDate,
                        endDate,
                        state,
=======
                GenerateFeeSummaryReportCommand.Output
                    partOutput =
                    this.generateFeeSummaryReportCommand.exportAll(new GenerateFeeSummaryReportCommand.Input(
                        startDate,
                        endDate,
>>>>>>> Stashed changes
                        dfspId,
                        fileType,
                        timezoneOffset,
                        offset,
                        rowsInPart), rowsInPart, pageSize);

                String entryName = baseFileName + "-Part" + partNumber + "." + fileType;
                ZipEntry entry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(entry);
<<<<<<< Updated upstream
                zipOutputStream.write(partOutput.transactionDetailRptByte());
                zipOutputStream.closeEntry();
                LOGGER.info("Generated transaction detail report part [{}] with [{}] rows", partNumber, rowsInPart);
=======
                zipOutputStream.write(partOutput.feeSummaryRptByte());
                zipOutputStream.closeEntry();
                LOGGER.info("Generated fee summary report part [{}] with [{}] rows", partNumber, rowsInPart);
>>>>>>> Stashed changes
                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }

    private String baseFileName(ReportDownloadRequest request) {

        String timestamp = FILE_DATE_FORMAT.format(LocalDateTime.now(ZoneOffset.UTC));
        return request.getReportType().name() + "-" + request.getId().getEntityId() + "-" + timestamp;
    }

}
