package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class DfspDailyTransactionTrendSummary {

    private final JdbcTemplate centralLedgerJdbcTemplate;

    public DfspDailyTransactionTrendSummary(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        assert jdbcTemplate != null;

        this.centralLedgerJdbcTemplate = jdbcTemplate;

    }

    public void generate(String fspId, String startdate, String enddate, String fileType,
                         OutputStream destination)
            throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dfspId", fspId);
        params.put("startDate", startdate);
        params.put("endDate", enddate);

        InputStream
            settlementReport =
            this.getClass()
                .getResourceAsStream(
                    "/com/thitsaworks/operation_portal/reporting/report/report/settlementStatement.jasper");

        try (Connection conn = this.centralLedgerJdbcTemplate.getDataSource()
                                                             .getConnection()) {

            if (fileType.equalsIgnoreCase(".pdf")) {

                JasperPrint jasperPrint = JasperFillManager.fillReport(settlementReport, params,
                                                                       conn);
                JRPdfExporter exporter = new JRPdfExporter();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destination));

                SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();

                reportConfig.setSizePageToContent(true);
                reportConfig.setForceLineBreakPolicy(false);

                SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();

                exportConfig.setMetadataAuthor("ThitsaWorks");
                exportConfig.setEncrypted(true);
                exportConfig.setAllowedPermissionsHint("PRINTING");

                exporter.setConfiguration(reportConfig);
                exporter.setConfiguration(exportConfig);

                exporter.exportReport();

                settlementReport.close();
            }
            if (fileType.equalsIgnoreCase(".xlsx")) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(settlementReport, params,
                                                                       conn);
                JRXlsxExporter exporter = new JRXlsxExporter();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(destination));
                SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
                exportConfig.setMetadataAuthor("ThitsaWorks");
                exportConfig.setEncrypted(true);
                exportConfig.setAllowedPermissionsHint("PRINTING");

                exporter.exportReport();

                settlementReport.close();

            }
        } catch (Exception e) {

            throw new ReportException(ReportErrors.REPORT_FAILURE_EXCEPTION);
        } finally {
        }

    }

}
