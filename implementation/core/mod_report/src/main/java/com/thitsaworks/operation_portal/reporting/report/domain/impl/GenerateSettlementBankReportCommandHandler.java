package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
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
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateSettlementBankReportCommandHandler
    implements GenerateSettlementBankReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateSettlementBankReportCommandHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenerateSettlementBankReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("settlementId", input.settlementId());
        params.put("currencyId", input.currencyId());
        params.put("timezoneoffset", input.timezoneOffset());
        params.put("reportFileType", input.fileType());
        params.put("userName", input.userName());
        params.put("dfspId", input.dfspId());
        params.put("isParent", input.isParent());

        InputStream jrxmlStream = getClass()
                                      .getClassLoader()
                                      .getResourceAsStream(
                                          "com/thitsaworks/operation_portal/reporting/report/report/settlementBankReport.jrxml");

        try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()) {

            DatabaseMetaData md = conn.getMetaData();
            LOG.info(
                "URL={} | user={} | driver={} {}", md.getURL(), md.getUserName(),
                md.getDriverName(), md.getDriverVersion());

            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("settlementBankReport");

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

            JasperReport settlementBankReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                settlementBankReport, params,
                conn);

            if (jasperPrint.getPages() == null || jasperPrint.getPages().isEmpty()) {

                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            byte[] rptBytes = new byte[0];

            if (input.fileType().equalsIgnoreCase("xlsx")) {

                SimpleXlsxReportConfiguration xlsxConfig = new SimpleXlsxReportConfiguration();
                xlsxConfig.setIgnorePageMargins(true);

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
                xlsxExporter.setConfiguration(xlsxConfig);
                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

            } else if (input.fileType().equalsIgnoreCase("pdf")) {

                JRPdfExporter pdfExporter = new JRPdfExporter();
                ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReport));
                pdfExporter.exportReport();
                rptBytes = pdfReport.toByteArray();

            } else if (input.fileType().equalsIgnoreCase("csv")) {

                JRCsvExporter csvExporter = new JRCsvExporter();
                ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
                csvExporter.exportReport();
                rptBytes = csvReport.toByteArray();
            } else {

                throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
            }

            return new Output(rptBytes);

        } catch (ReportException e) {

            throw e;

        } catch (Exception e) {

            LOG.error("Error : [{}]", e.getMessage());
            throw new ReportException(ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION);

        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) FROM (
                    SELECT p.name
                    FROM settlement s
                    INNER JOIN settlementSettlementWindow ssw  ON  ssw.settlementId = s.settlementId
                    INNER JOIN transferFulfilment tf           ON  tf.settlementWindowId = ssw.settlementWindowId
                    INNER JOIN transferParticipant tp          ON  tp.transferId = tf.transferId
                    INNER JOIN participantCurrency pc          ON  tp.participantCurrencyId = pc.participantCurrencyId
                    INNER JOIN participant p                   ON  p.participantId = pc.participantId
                    LEFT JOIN operation_portal.tbl_participant op ON op.participant_name COLLATE UTF8MB4_UNICODE_CI  = p.name COLLATE UTF8MB4_UNICODE_CI
                    LEFT JOIN operation_portal.tbl_liquidity_profile lp ON lp.currency COLLATE UTF8MB4_UNICODE_CI  = pc.currencyId COLLATE UTF8MB4_UNICODE_CI  AND is_active = 1 AND op.participant_id COLLATE UTF8MB4_UNICODE_CI  = lp.participant_id  COLLATE UTF8MB4_UNICODE_CI
                    INNER JOIN ledgerAccountType lat           ON  lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
                    WHERE s.settlementId = ? AND lat.name = 'POSITION'
                      AND (? = 'All' COLLATE UTF8MB4_UNICODE_CI OR pc.currencyId COLLATE UTF8MB4_UNICODE_CI = ? COLLATE UTF8MB4_UNICODE_CI)
                    GROUP BY p.name, pc.participantCurrencyId, lp.bank_name, lp.account_name, lp.account_number, op.description
                ) x
                """, new Object[]{
                input.settlementId(),
                input.currencyId(),
                input.currencyId()}, Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

}
