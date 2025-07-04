package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
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
public class GenerateSettlementReportCommandHandler implements GenerateSettlementReportCommand {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenerateSettlementReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Reporting.WRITE_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dfspId", input.fspId());
        params.put("settlementId", input.settlementId());
        params.put("timezoneoffset", input.timezoneOffset());

        InputStream settlementReport =
            this.getClass()
                .getResourceAsStream("/com/thitsaworks/operation_portal/reporting/report/report/settlementReport.jasper");

        try (Connection conn = this.jdbcTemplate.getDataSource()
                                                .getConnection()) {

            JasperPrint jasperPrint = JasperFillManager.fillReport(settlementReport, params,
                                                                   conn);

            byte[] rptBytes = new byte[0];

            JRCsvExporter csvExporter = new JRCsvExporter();
            ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
            csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
            csvExporter.exportReport();
            rptBytes = csvReport.toByteArray();

            if (input.filetype()
                     .equalsIgnoreCase("xlsx") && rptBytes.length > 0) {

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

            }

            return new Output(rptBytes);

        } catch (Exception e) {

            throw new ReportException(ReportErrors.REPORT_FAILURE_EXCEPTION);
        }
    }

}
