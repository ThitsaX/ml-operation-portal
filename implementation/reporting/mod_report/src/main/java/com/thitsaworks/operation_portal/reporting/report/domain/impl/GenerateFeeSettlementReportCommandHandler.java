package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSettlementReportCommand;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateFeeSettlementReportCommandHandler implements GenerateFeeSettlementReportCommand {

    @Autowired
    private JdbcTemplate centralLedgerJdbcTemplate;

    @Override
    public Output execute(Input input) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startdate", input.getStartDate());
        params.put("enddate", input.getEndDate());
        params.put("fromfspid", input.getFromFsp());
        params.put("tofspid", input.getToFsp());
        params.put("currency", input.getCurrency());
        params.put("timezoneoffset", input.getTimezone());

        InputStream settlementReport = this.getClass().getResourceAsStream(
                "/com/thitsa/dfsp_portal/report/report/feeSettlementReport.jasper");
        Connection conn = this.centralLedgerJdbcTemplate.getDataSource().getConnection();

        try {
            byte[] rptBytes = new byte[0];

            JRCsvExporter csvExporter = new JRCsvExporter();
            ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
            JasperPrint jp = JasperFillManager.fillReport(settlementReport, params,
                                                          conn);
            csvExporter.setExporterInput(new SimpleExporterInput(jp));
            csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
            csvExporter.exportReport();
            rptBytes = csvReport.toByteArray();

            if (input.getFileType().equalsIgnoreCase("xlsx") && rptBytes.length > 0) {
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

        } finally {
            conn.close();
        }
    }

}
