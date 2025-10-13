package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateSettlementAuditReportCommandHandler implements GenerateSettlementAuditReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementAuditReportCommandHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenerateSettlementAuditReportCommandHandler(
            @Qualifier(PersistenceQualifiers.Hub.WRITE_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startDate", input.startDate().toString());
        params.put("endDate", input.endDate().toString());
        params.put("dfspId", input.dfspId());
        params.put("dfspName", input.dfspName());
        params.put("currencyId", input.currencyId());
        params.put("timezoneoffset", input.timeZoneOffset());

        LOG.info("Params : {}", params);

        InputStream settlementAuditReport =
                this.getClass()
                    .getResourceAsStream(
                            "com/thitsaworks/operation_portal/reporting/report/report/settlementAuditReport.jasper");

        try (Connection conn = this.jdbcTemplate.getDataSource()
                                                .getConnection()) {

            JasperPrint jasperPrint = JasperFillManager.fillReport(settlementAuditReport, params,
                                                                   conn);

            byte[] rptBytes = new byte[0];

            if (input.filetype()
                     .equalsIgnoreCase("xlsx")) {

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));

                SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
                config.setOnePagePerSheet(false);
                config.setDetectCellType(true);
                config.setRemoveEmptySpaceBetweenRows(true);
                config.setIgnorePageMargins(true);
                config.setCollapseRowSpan(false);

                xlsxExporter.setConfiguration(config);

                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

            } else if (input.filetype()
                            .equalsIgnoreCase("pdf")) {

                JRPdfExporter pdfExporter = new JRPdfExporter();
                ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReport));
                pdfExporter.exportReport();
                rptBytes = pdfReport.toByteArray();

            } else if (input.filetype()
                            .equalsIgnoreCase("csv")) {

                JRCsvExporter csvExporter = new JRCsvExporter();
                ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
                csvExporter.exportReport();
                rptBytes = csvReport.toByteArray();
            }

            return new Output(rptBytes);

        } catch (Exception e) {

            LOG.info("Error : [{}]", e.getMessage());
            throw new ReportException(ReportErrors.SETTLEMENT_AUDIT_REPORT_FAILURE_EXCEPTION);
        }
    }

}
