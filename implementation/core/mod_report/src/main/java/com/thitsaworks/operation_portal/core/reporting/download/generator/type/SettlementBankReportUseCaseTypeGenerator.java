package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankReportUseCaseCommand;
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
class SettlementBankReportUseCaseTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementBankReportUseCaseTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;
    private static final String DEFAULT_DFSP_ID = "hub";

    private final GenerateSettlementBankReportUseCaseCommand generateSettlementBankReportUseCaseCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {
        return ReportType.SETTLEMENT_BANK_USECASE;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String settlementId = this.reportGeneratorSupport.requireParam(params, "settlementId");
        String currencyId = params.getOrDefault("currencyId", "ALL");
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String userName = params.getOrDefault("userName", "");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());
        String dfspId = params.getOrDefault("dfspId", DEFAULT_DFSP_ID);
        if (dfspId == null || dfspId.isBlank()) {
            dfspId = DEFAULT_DFSP_ID;
        }
        boolean isParent = Boolean.parseBoolean(params.getOrDefault("isParent", String.valueOf(false)));

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementBankReportUseCaseCommand.countRows(
            new GenerateSettlementBankReportUseCaseCommand.CountInput(settlementId, currencyId, dfspId));

        LOGGER.info("Total Settlement Bank UseCase Row Count : [{}]", totalRowCount);

        if (totalRowCount <= pageSize) {
            GenerateSettlementBankReportUseCaseCommand.Output output =
                this.generateSettlementBankReportUseCaseCommand.execute(
                    new GenerateSettlementBankReportUseCaseCommand.Input(
                        settlementId, currencyId, fileType, timezoneOffset, userName, dfspId, isParent, 0,
                        Integer.MAX_VALUE));

            return new ReportGeneratedFile(output.settlementBankRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(
                settlementId, currencyId, fileType, timezoneOffset, userName, dfspId, isParent, totalRowCount);
        }

        GenerateSettlementBankReportUseCaseCommand.Output output =
            this.generateSettlementBankReportUseCaseCommand.execute(
                new GenerateSettlementBankReportUseCaseCommand.Input(
                    settlementId, currencyId, fileType, timezoneOffset, userName, dfspId, isParent, 0,
                    Integer.MAX_VALUE));

        return new ReportGeneratedFile(output.settlementBankRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(String settlementId,
                                                 String currencyId,
                                                 String fileType,
                                                 String timezoneOffset,
                                                 String userName,
                                                 String dfspId,
                                                 boolean isParent,
                                                 int totalRowCount) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateSettlementBankReportUseCaseCommand.Output output =
                    this.generateSettlementBankReportUseCaseCommand.execute(
                        new GenerateSettlementBankReportUseCaseCommand.Input(
                            settlementId,
                            currencyId,
                            fileType,
                            timezoneOffset,
                            userName,
                            dfspId,
                            isParent,
                            offset,
                            rowsInPart));

                String entryName = "settlement_bank_usecase_part_" + partNumber + "." + fileType;
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
