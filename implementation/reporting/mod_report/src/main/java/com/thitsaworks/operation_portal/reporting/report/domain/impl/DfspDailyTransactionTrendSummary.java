package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import net.sf.jasperreports.engine.JRException;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DfspDailyTransactionTrendSummary {

    private JdbcTemplate centralLedgerJdbcTemplate;

    public DfspDailyTransactionTrendSummary(
            @Qualifier(PersistenceQualifiers.Reporting.Read_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        assert jdbcTemplate != null;

        this.centralLedgerJdbcTemplate = jdbcTemplate;

    }

    public void generate(String fspId, String startdate, String enddate, String fileType,
                         OutputStream destination)
            throws JRException, IOException, SQLException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dfspId", fspId);
        params.put("startDate", startdate);
        params.put("endDate", enddate);

        InputStream settlementReport = this.getClass().getResourceAsStream(
                "/com/thitsa/dfsp_portal/report/report/settlementStatement.jasper");

        Connection conn = this.centralLedgerJdbcTemplate.getDataSource().getConnection();

        if (fileType.toLowerCase() == ".pdf") {

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
        if (fileType.toLowerCase() == ".xlsx") {
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

    }

}
