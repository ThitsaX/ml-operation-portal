package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateDfspDailyTransactionTrendSummaryCommand;
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
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateDfspDailyTransactionTrendSummaryCommandHandler implements
                                                                    GenerateDfspDailyTransactionTrendSummaryCommand {

    private final JdbcTemplate centralLedgerJdbcTemplate;

    public GenerateDfspDailyTransactionTrendSummaryCommandHandler(
        @Qualifier(PersistenceQualifiers.Reporting.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.centralLedgerJdbcTemplate = jdbcTemplate;

    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dfspId", input.fspId());
        params.put("startDate", input.startdate());
        params.put("endDate", input.enddate());

        InputStream
            settlementReport =
            this.getClass()
                .getResourceAsStream(
                    "/com/thitsaworks/operation_portal/reporting/report/report/settlementStatement.jasper");

        try (Connection conn = this.centralLedgerJdbcTemplate.getDataSource()
                                                             .getConnection()) {

            if (input.fileType().equalsIgnoreCase(".pdf")) {

                JasperPrint jasperPrint = JasperFillManager.fillReport(settlementReport, params,
                                                                       conn);
                JRPdfExporter exporter = new JRPdfExporter();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(input.destination()));

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
            if (input.fileType().equalsIgnoreCase(".xlsx")) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(settlementReport, params,
                                                                       conn);
                JRXlsxExporter exporter = new JRXlsxExporter();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(input.destination()));
                SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
                exportConfig.setMetadataAuthor("ThitsaWorks");
                exportConfig.setEncrypted(true);
                exportConfig.setAllowedPermissionsHint("PRINTING");

                exporter.exportReport();

                settlementReport.close();

            }
            return new Output(Boolean.TRUE);

        } catch (Exception e) {

            throw new ReportException(ReportErrors.DAILY_TRANSACTION_SUMMARY_REPORT_FAILURE_EXCEPTION);
        }

    }

}
