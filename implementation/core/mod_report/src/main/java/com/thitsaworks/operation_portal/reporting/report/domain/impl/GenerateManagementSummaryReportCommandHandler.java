package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateManagementSummaryReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateManagementSummaryReportCommandHandler implements GenerateManagementSummaryReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateManagementSummaryReportCommandHandler.class);
    private final JdbcTemplate jdbcTemplate;
    private static final int MAX_REPORT_ROWS = 20000;

    public GenerateManagementSummaryReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startDate" , input.startDate());
        params.put("endDate" , input.endDate());
        params.put("timezoneOffset" , input.timezoneOffset());
        params.put("reportFileType" , input.fileType());
        params.put("userName" , input.userName());

        // ─── ROW COUNT CHECK ──────────────────────────────────────────────────────
        String countQuery = """
                SELECT COUNT(*) FROM (
                    SELECT
                        COUNT(1) AS NumberOfTransactions
                    FROM transfer t
                    JOIN transferStateChange tsc
                         ON t.transferId = tsc.transferId
                        AND tsc.transferStateId = 'COMMITTED'
                    INNER JOIN transferParticipant tpayer
                         ON t.transferId = tpayer.transferId
                        AND tpayer.transferParticipantRoleTypeId = 1
                    INNER JOIN participant payer
                         ON payer.participantId = tpayer.participantId
                    INNER JOIN transferParticipant tpayee
                         ON t.transferId = tpayee.transferId
                        AND tpayee.transferParticipantRoleTypeId = 2
                    INNER JOIN participant payee
                         ON payee.participantId = tpayee.participantId
                    INNER JOIN currency c
                         ON c.currencyId = t.currencyId
                    WHERE t.createdDate BETWEEN
                          STR_TO_DATE(?, '%Y-%m-%dT%T')
                      AND STR_TO_DATE(?, '%Y-%m-%dT%T')
                    GROUP BY
                        tpayer.participantId,
                        tpayee.participantId,
                        t.currencyId
                ) x
                """;

        Integer rowCount = jdbcTemplate.queryForObject(
            countQuery,
            new Object[]{ input.startDate(), input.endDate() },
            Integer.class);

        if (rowCount != null && rowCount > MAX_REPORT_ROWS) {
            throw new ReportException(ReportErrors.REPORT_MAXIMUM_LIMIT_EXCEPTION.format(rowCount, MAX_REPORT_ROWS));
        }

        InputStream jrxmlStream = this.getClass().getClassLoader()
                                      .getResourceAsStream("com/thitsaworks/operation_portal/reporting/report/report/managementSummaryReport.jrxml");

        try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()) {
            DatabaseMetaData md = conn.getMetaData();
            LOG.info("URL={} | user={} | driver={} {}",
                     md.getURL(),
                     md.getUserName(),
                     md.getDriverName(),
                     md.getDriverVersion());

            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("managementSummaryReport");

            // Remove pageFooter for Excel, keep for PDF
            if (input.fileType().equalsIgnoreCase("xlsx")) {
                JRDesignBand pageFooter = (JRDesignBand) design.getPageFooter();
                if (pageFooter != null) {
                    pageFooter.setHeight(0);
                    // Remove all elements from the band
                    for (int i = pageFooter.getChildren().size() - 1; i >= 0; i--) {
                        pageFooter.removeElement((JRDesignElement) pageFooter.getChildren().get(i));
                    }
                }
            }

            JasperReport detailReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(detailReport, params, conn);

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
            } else if (input.fileType().equalsIgnoreCase("pdf")) {
                JRPdfExporter pdfExporter = new JRPdfExporter();
                ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReport));
                pdfExporter.exportReport();
                rptBytes = pdfReport.toByteArray();
            } else {
                throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
            }
            return new Output(rptBytes);
        } catch (ReportException e) {

            throw e;

        } catch (Exception e) {

            LOG.error("Error : [{}]", e.getMessage());
            throw new ReportException(ReportErrors.MANAGEMENT_SUMMARY_REPORT_FAILURE_EXCEPTION);

        }
    }

}



























