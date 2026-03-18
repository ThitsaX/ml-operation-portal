package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.logging.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
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
@NoLogging
public class GenerateSettlementReportCommandHandler implements GenerateSettlementReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateSettlementReportCommandHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenerateSettlementReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dfspId", input.fspId());
        params.put("dfspName", input.fspName());
        params.put("settlementId", input.settlementId());
        params.put("timezoneoffset", input.timezoneOffset());
        params.put("report", input.filetype());
        params.put("user", input.userName());
        params.put("offset", input.offset() == null ? 0 : input.offset());
        params.put("limit", input.limit());

        InputStream jrxmlStream = this
                                      .getClass()
                                      .getClassLoader()
                                      .getResourceAsStream(
                                          "com/thitsaworks/operation_portal/reporting/report/report/settlementReport.jrxml");

        try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()) {

            DatabaseMetaData md = conn.getMetaData();
            LOG.info(
                "URL={} | user={} | driver={} {}", md.getURL(), md.getUserName(),
                md.getDriverName(), md.getDriverVersion());

            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("settlementReport");
            JasperReport settlementReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(settlementReport, params, conn);

            if (jasperPrint.getPages() == null || jasperPrint.getPages().isEmpty()) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            byte[] rptBytes = new byte[0];

            if (input.filetype().equalsIgnoreCase("xlsx")) {

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));

                SimpleXlsxReportConfiguration cfg = new SimpleXlsxReportConfiguration();
                cfg.setDetectCellType(true);
                cfg.setWhitePageBackground(false);
                cfg.setOnePagePerSheet(false);
                cfg.setCollapseRowSpan(false);
                cfg.setWrapText(true);
                cfg.setIgnoreGraphics(false);
                xlsxExporter.setConfiguration(cfg);

                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

            } else if (input.filetype().equalsIgnoreCase("pdf")) {

                JRPdfExporter pdfExporter = new JRPdfExporter();
                ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReport));

                SimplePdfReportConfiguration pdfReportCfg = new SimplePdfReportConfiguration();
                pdfReportCfg.setSizePageToContent(true);   // trims empty page whitespace
                pdfReportCfg.setForceSvgShapes(false);     // keep text as text (crisper)
                pdfExporter.setConfiguration(pdfReportCfg);

                SimplePdfExporterConfiguration pdfExportCfg = new SimplePdfExporterConfiguration();
                pdfExportCfg.setMetadataTitle("Settlement Report");
                pdfExportCfg.setMetadataAuthor("Hub Operator");

                pdfExporter.setConfiguration(pdfExportCfg);

                pdfExporter.exportReport();
                rptBytes = pdfReport.toByteArray();

            } else if (input.filetype().equalsIgnoreCase("csv")) {

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

            LOG.info("Error : [{}]", e.getMessage());
            throw new ReportException(ReportErrors.SETTLEMENT_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) FROM (
                    SELECT s3.participantId, s3.currencyId
                    FROM (
                        SELECT
                            IF(senderName != ?, senderId, receiverId) as participantId,
                            s.currencyId
                        FROM (
                            SELECT MAX(CASE WHEN tP.amount > 0 THEN p.participantId END) as senderId,
                                   MAX(CASE WHEN tP.amount < 0 THEN p.participantId END) as receiverId,
                                   MAX(CASE WHEN tP.amount > 0 THEN p.name END)          as senderName,
                                   MAX(CASE WHEN tP.amount < 0 THEN p.name END)          as receiverName,
                                   pC.currencyId
                            FROM transferParticipant tP
                            INNER JOIN transferFulfilment tF on tP.transferId = tF.transferId
                            INNER JOIN settlementSettlementWindow sSW on tF.settlementWindowId = sSW.settlementWindowId
                            INNER JOIN settlementWindowStateChange sWSC on sSW.settlementWindowId = sWSC.settlementWindowId
                            INNER JOIN settlement s on sSW.settlementId = s.settlementId
                            INNER JOIN participantCurrency pC on tP.participantCurrencyId = pC.participantCurrencyId
                            INNER JOIN participant p on pC.participantId = p.participantId
                            WHERE tF.isValid
                              AND sWSC.settlementWindowStateId = 'CLOSED'
                              AND s.settlementId = ?
                            GROUP BY tF.transferId, s.settlementId, pC.currencyId
                        ) s
                        WHERE s.senderName = ? OR s.receiverName = ?
                        GROUP BY IF(senderName != ?, senderId, receiverId), s.currencyId
                    ) s3
                ) x
                """, new Object[]{
                input.fspId(),
                input.settlementId(),
                input.fspId(),
                input.fspId(),
                input.fspId()}, Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

}
