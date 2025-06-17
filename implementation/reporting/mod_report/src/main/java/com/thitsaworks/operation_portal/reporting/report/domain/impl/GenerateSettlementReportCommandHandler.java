package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateSettlementReportCommandHandler implements GenerateSettlementReportCommand {

    @Autowired
    private JdbcTemplate centralLedgerJdbcTemplate;

    @Override
    public Output execute(Input input) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dfspId", input.getFspId());
        params.put("settlementId", input.getSettlementId());
        params.put("timezoneoffset", input.getTimezoneOffset());

        InputStream settlementReport =
                this.getClass().getResourceAsStream("/com/thitsa/dfsp_portal/report/report/settlementReport.jasper");

        Connection conn = this.centralLedgerJdbcTemplate.getDataSource().getConnection();

        JasperPrint jasperPrint = JasperFillManager.fillReport(settlementReport, params,
                                                               conn);

        byte[] rptBytes = new byte[0];

        try {

            JRCsvExporter csvExporter = new JRCsvExporter();
            ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
            csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
            csvExporter.exportReport();
            rptBytes = csvReport.toByteArray();

            if (input.getFiletype().equalsIgnoreCase("xlsx") && rptBytes.length > 0) {

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

            }

            return new Output(rptBytes);

        } finally {

            conn.close();

        }
    }

}
