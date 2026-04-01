package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenerateAuditReportCommandHandler implements GenerateAuditReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateAuditReportCommandHandler.class);

    private final JdbcTemplate jdbcTemplate;

    private static final int MAX_REPORT_ROWS = 20000;

    public GenerateAuditReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Core.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()) {

            var timeOffset = input.timezoneOffset();

            params.put("timezoneoffset", timeOffset);
            params.put("fromDate", input.fromDate().getEpochSecond());
            params.put("toDate", input.toDate().getEpochSecond());

            if (input.realmId() != null) {
                params.put("realmId", input.realmId());
            }

            if (input.userId() != null) {
                params.put("userId", input.userId());
            }

            if (input.actionId() != null) {
                params.put("actionId", input.actionId());
            }

            params.put("grantedActionList", input.grantedActionList());

            String inSql = "";

            List<String> grantedActionList = input.grantedActionList();

            if (grantedActionList != null && !grantedActionList.isEmpty()) {
                String placeholders = grantedActionList
                                          .stream()
                                          .map(id -> "?")
                                          .collect(Collectors.joining(", "));
                inSql = " AND tac.action_id IN (" + placeholders + ") ";
            }
            String reportQuery = """
                    SELECT COUNT(tac.action_code) AS rowAccount
                    FROM tbl_audit AS ta
                    LEFT JOIN tbl_participant AS tp ON tp.participant_id = ta.participant_id
                    LEFT JOIN tbl_user AS tu ON tu.user_id = ta.user_id
                    JOIN tbl_action AS tac ON tac.action_id = ta.action_id
                    WHERE
                       (? IS NULL OR ? = '' OR ta.participant_id = ?)
                       AND (
                           (? IS NULL OR ? IS NULL)
                           OR (ta.created_date BETWEEN ? AND ?)
                       )
                       AND (? IS NULL OR ? = '' OR ta.user_id = ?)
                       AND (? IS NULL OR ? = '' OR tac.action_id = ?)
                """ + inSql;

            List<Object> objectList = new ArrayList<>();

            objectList.add(input.realmId());
            objectList.add(input.realmId());
            objectList.add(input.realmId());

            objectList.add(input.fromDate().getEpochSecond());
            objectList.add(input.toDate().getEpochSecond());
            objectList.add(input.fromDate().getEpochSecond());
            objectList.add(input.toDate().getEpochSecond());

            objectList.add(input.userId());
            objectList.add(input.userId());
            objectList.add(input.userId());

            objectList.add(input.actionId());
            objectList.add(input.actionId());
            objectList.add(input.actionId());

            if (grantedActionList != null && !grantedActionList.isEmpty()) {
                objectList.addAll(grantedActionList);
            }

            Integer rowCount = jdbcTemplate.queryForObject(reportQuery, objectList.toArray(),
                Integer.class);

            LOG.info("Row Count: {}", rowCount);

            if (rowCount != null && rowCount > MAX_REPORT_ROWS) {

                throw new ReportException(
                    ReportErrors.REPORT_MAXIMUM_LIMIT_EXCEPTION.format(rowCount, MAX_REPORT_ROWS));
            }

            InputStream jrxmlStream = getClass()
                                          .getClassLoader()
                                          .getResourceAsStream(
                                              "com/thitsaworks/operation_portal/reporting/report/report/auditReport.jrxml");

            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("auditReport");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);

            if (jasperPrint.getPages() == null || jasperPrint.getPages().isEmpty()) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            byte[] rptBytes = new byte[0];

            if (input.fileType().equalsIgnoreCase("xlsx")) {

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

            } else if (input.fileType().equalsIgnoreCase("csv")) {

                JRCsvExporter csvExporter = new JRCsvExporter();
                ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
                csvExporter.exportReport();

                String csvContent = csvReport.toString(StandardCharsets.UTF_8);
                csvContent = "\n" + csvContent;

                rptBytes = csvContent.getBytes(StandardCharsets.UTF_8);

            } else {

                throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
            }

            return new Output(rptBytes);

        } catch (ReportException e) {

            throw e;
        } catch (Exception e) {

            LOG.info("Error : [{}]", e.getMessage());
            throw new ReportException(ReportErrors.AUDIT_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public Output exportAll(Input input, int totalRowCount, int pageSize) throws ReportException {

        return GenerateAuditReportCommand.super.exportAll(input, totalRowCount, pageSize);
    }

    @Override
    public int countRows(CountInput input) {

        return 0;
    }

}
