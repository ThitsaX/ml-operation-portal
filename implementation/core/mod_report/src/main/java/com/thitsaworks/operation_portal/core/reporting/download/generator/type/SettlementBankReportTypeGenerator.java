package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.PagedZipSupport;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@Component
@RequiredArgsConstructor
class SettlementBankReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementBankReportTypeGenerator.class);

    private final GenerateSettlementBankReportCommand generateSettlementBankReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final PagedZipSupport pagedZipSupport;

    private final ReportGenerator.Settings settings;


    @Override
    public ReportType reportType() {

        return ReportType.SETTLEMENT_BANK;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String settlementId = this.reportGeneratorSupport.requireParam(params, "settlementId");
        String currencyId = this.reportGeneratorSupport
                                .requireParam(params, "currencyId")
                                .toUpperCase(Locale.ROOT);
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String userName = params.getOrDefault("userName", "System");
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateSettlementBankReportCommand.countRows(
            new GenerateSettlementBankReportCommand.CountInput(settlementId, currencyId));

        LOGGER.info("Total Row Count : [{}]", totalRowCount);


        if (totalRowCount <= pageSize) {
            GenerateSettlementBankReportCommand.Output output = this.generateSettlementBankReportCommand.execute(
                new GenerateSettlementBankReportCommand.Input(
                    settlementId, currencyId, fileType,
                    timezoneOffset, userName, 0, pageSize));

            return new ReportGeneratedFile(output.settlementBankRptByte(), fileType);
        }

        return this.pagedZipSupport.generatePagedZip(
            "settlement_bank_part_", fileType, totalRowCount, pageSize,
            (offset, limit) -> this.generateSettlementBankReportCommand
                                   .execute(
                                       new GenerateSettlementBankReportCommand.Input(
                                           settlementId,
                                           currencyId, fileType, timezoneOffset, userName, offset,
                                           limit))
                                   .settlementBankRptByte());
    }

}

