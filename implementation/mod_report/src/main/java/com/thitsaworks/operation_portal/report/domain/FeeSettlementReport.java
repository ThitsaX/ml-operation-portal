package com.thitsaworks.operation_portal.report.domain;

import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadTransactional;
import net.sf.jasperreports.engine.JRException;
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
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Service
@CentralLedgerReadTransactional
public class FeeSettlementReport {

    @Autowired
    private JdbcTemplate centralLedgerJdbcTemplate;


    public byte[] generate(String startDate, String endDate, String fromFspId, String toFspId,String currency, String timeZone , String fileType )
            throws JRException, IOException, SQLException, ParseException {


        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startdate", startDate);
        params.put("enddate", endDate);
        params.put("fromfspid", fromFspId);
        params.put("tofspid", toFspId);
        params.put("currency", currency);
        params.put("timezoneoffset", timeZone);

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

             if (fileType.equalsIgnoreCase("xlsx") && rptBytes.length>0) {
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

            return rptBytes;

        }finally {
            conn.close();
        }

    }


}
