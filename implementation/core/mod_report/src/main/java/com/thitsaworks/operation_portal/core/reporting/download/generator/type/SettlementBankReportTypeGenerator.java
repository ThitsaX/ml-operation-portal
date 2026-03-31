package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankReportCommand;
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
class SettlementBankReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementBankReportTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;

    private final GenerateSettlementBankReportCommand generateSettlementBankReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {
        return ReportType.SETTLEMENT_BANK;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String settlementId = this.reportGeneratorSupport.requireParam(params, "settlementId");
        String currencyId = params.getOrDefault("currencyId", "ALL");
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String userName = params.getOrDefault("userName", "");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementBankReportCommand.countRows(
            new GenerateSettlementBankReportCommand.CountInput(settlementId, currencyId));

        LOGGER.info("Total Settlement Bank Row Count : [{}]", totalRowCount);

        if (totalRowCount <= pageSize) {
            GenerateSettlementBankReportCommand.Output output =
                this.generateSettlementBankReportCommand.execute(
                    new GenerateSettlementBankReportCommand.Input(
                        settlementId, currencyId, fileType, timezoneOffset, userName, 0, Integer.MAX_VALUE));

            return new ReportGeneratedFile(output.settlementBankRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(
                settlementId, currencyId, fileType, timezoneOffset, userName, totalRowCount);
        }

        GenerateSettlementBankReportCommand.Output output =
            this.generateSettlementBankReportCommand.execute(
                new GenerateSettlementBankReportCommand.Input(
                    settlementId, currencyId, fileType, timezoneOffset, userName, 0, Integer.MAX_VALUE));

        return new ReportGeneratedFile(output.settlementBankRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(String settlementId,
                                                 String currencyId,
                                                 String fileType,
                                                 String timezoneOffset,
                                                 String userName,
                                                 int totalRowCount) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateSettlementBankReportCommand.Output output =
                    this.generateSettlementBankReportCommand.execute(
                        new GenerateSettlementBankReportCommand.Input(
                            settlementId, currencyId, fileType, timezoneOffset, userName, offset, rowsInPart));

                String entryName = "settlement_bank_part_" + partNumber + "." + fileType;
                zipOutputStream.putNextEntry(new ZipEntry(entryName));
                zipOutputStream.write(output.settlementBankRptByte());
                zipOutputStream.closeEntry();
                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }
}
