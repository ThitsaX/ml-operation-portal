package com.thitsaworks.operation_portal.core.reporting.download.generator.type;

import com.thitsaworks.operation_portal.component.common.type.ReportType;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGeneratedFile;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.ReportTypeGenerator;
import com.thitsaworks.operation_portal.core.reporting.download.generator.support.ReportGeneratorSupport;
import com.thitsaworks.operation_portal.core.reporting.download.model.ReportDownloadRequest;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
@RequiredArgsConstructor
class AuditReportTypeGenerator implements ReportTypeGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditReportTypeGenerator.class);

    private static final int MAX_ROWS_PER_REPORT_FILE = 500_000;

    private final GenerateAuditReportCommand generateAuditReportCommand;

    private final ReportGeneratorSupport reportGeneratorSupport;

    private final ReportGenerator.Settings settings;

    @Override
    public ReportType reportType() {

        return ReportType.AUDIT;
    }

    @Override
    public ReportGeneratedFile generate(ReportDownloadRequest request, Map<String, String> params)
        throws ReportException, IOException {

        String realmId = this.reportGeneratorSupport.normalizeAllToken(params.get("realmId"));
        Instant fromDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "fromDate"));
        Instant toDate = Instant.parse(this.reportGeneratorSupport.requireParam(params, "toDate"));
        String timezoneOffset = params.getOrDefault("timezoneOffset", "+0000");
        String userId = this.reportGeneratorSupport.normalizeAllToken(params.get("userId"));
        String actionId = this.reportGeneratorSupport.normalizeAllToken(params.get("actionId"));
        String fileType = this.reportGeneratorSupport.fileType(request.getFileType());
        List<String> grantedActionList = Arrays.asList(params.getOrDefault("grantedActionList", "").split(","));

        int pageSize = this.settings.reportPageSize();
        int totalRowCount = this.generateAuditReportCommand.countRows(new GenerateAuditReportCommand.CountInput(
                realmId,
                fromDate,
                toDate,
                userId,
                actionId,
                grantedActionList));

        LOGGER.info("Total Row Count : [{}]", totalRowCount);

        GenerateAuditReportCommand.Input input = new GenerateAuditReportCommand.Input(realmId,
                                                                                      fromDate,
                                                                                      toDate,
                                                                                      timezoneOffset,
                                                                                      userId,
                                                                                      actionId,
                                                                                      fileType,
                                                                                      grantedActionList,
                                                                                      0,
                                                                                      pageSize);
        if (totalRowCount <= pageSize) {
            GenerateAuditReportCommand.Output output = this.generateAuditReportCommand.execute(
                input);

            return new ReportGeneratedFile(output.auditRptByte(), fileType);
        }

        if (totalRowCount > MAX_ROWS_PER_REPORT_FILE) {
            return this.generateSplitZip(realmId,
                                         fromDate,
                                         toDate,
                                         timezoneOffset,
                                         userId,
                                         actionId,
                                         fileType,
                                         grantedActionList,
                                         totalRowCount,
                                         pageSize);
        }

        GenerateAuditReportCommand.Output output = this.generateAuditReportCommand.exportAll(
            input,
            totalRowCount,
            pageSize);

        return new ReportGeneratedFile(output.auditRptByte(), fileType);
    }

    private ReportGeneratedFile generateSplitZip(String realmId,
                                                 Instant fromDate,
                                                 Instant toDate,
                                                 String timezoneOffset,
                                                 String userId,
                                                 String actionId,
                                                 String fileType,
                                                 List<String> grantedActionList,
                                                 int totalRowCount,
                                                 int pageSize) throws ReportException, IOException {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            int partNumber = 1;
            for (int offset = 0; offset < totalRowCount; offset += MAX_ROWS_PER_REPORT_FILE) {
                int rowsInPart = Math.min(MAX_ROWS_PER_REPORT_FILE, totalRowCount - offset);

                GenerateAuditReportCommand.Output
                    partOutput =
                    this.generateAuditReportCommand.exportAll(new GenerateAuditReportCommand.Input(
                        realmId,
                        fromDate,
                        toDate,
                        timezoneOffset,
                        userId,
                        actionId,
                        fileType,
                        grantedActionList,
                        offset,
                        rowsInPart), rowsInPart, pageSize);

                String entryName = "audit_report_part_" + partNumber + "." + fileType;
                ZipEntry entry = new ZipEntry(entryName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(partOutput.auditRptByte());
                zipOutputStream.closeEntry();
                LOGGER.info("Generated audit report part [{}] with [{}] rows", partNumber, rowsInPart);
                partNumber++;
            }

            zipOutputStream.finish();
            return new ReportGeneratedFile(byteArrayOutputStream.toByteArray(), "zip");
        }
    }

}
