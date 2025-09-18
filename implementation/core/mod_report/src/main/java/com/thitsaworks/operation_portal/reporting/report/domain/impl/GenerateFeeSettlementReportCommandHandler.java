package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSettlementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
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
public class GenerateFeeSettlementReportCommandHandler implements GenerateFeeSettlementReportCommand {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenerateFeeSettlementReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Reporting.WRITE_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startdate", input.startDate());
        params.put("enddate", input.endDate());
        params.put("fromfspid", input.fromFsp());
        params.put("tofspid", input.toFsp());
        params.put("currency", input.currency());
        params.put("timezoneoffset", input.timezone());

        InputStream
            settlementReport =
            this.getClass()
                .getResourceAsStream(
                    "/com/thitsaworks/operation_portal/reporting/report/report/feeSettlementReport.jasper");

        try (Connection conn = this.jdbcTemplate.getDataSource()
                                                .getConnection()) {

            byte[] rptBytes = new byte[0];

            JRCsvExporter csvExporter = new JRCsvExporter();
            ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
            JasperPrint jp = JasperFillManager.fillReport(settlementReport, params,
                                                          conn);
            csvExporter.setExporterInput(new SimpleExporterInput(jp));
            csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
            csvExporter.exportReport();
            rptBytes = csvReport.toByteArray();

            if (input.fileType()
                     .equalsIgnoreCase("xlsx") && rptBytes.length > 0) {
                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jp));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
                SimpleXlsxReportConfiguration xlsxreportConfig = new SimpleXlsxReportConfiguration();
                xlsxreportConfig.setOnePagePerSheet(true);
                String[] SheetNames = {"Detailed", "Summary"};
                xlsxreportConfig.setSheetNames(SheetNames);
                xlsxExporter.setConfiguration(xlsxreportConfig);
                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

            }

            return new Output(rptBytes);

        } catch (Exception e) {
            throw new ReportException(ReportErrors.FEE_SETTLEMENT_REPORT_FAILURE_EXCEPTION);
        }
    }

}
