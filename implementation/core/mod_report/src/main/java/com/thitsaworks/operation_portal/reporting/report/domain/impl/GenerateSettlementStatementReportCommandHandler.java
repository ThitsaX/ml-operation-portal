package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementStatementReportCommand;
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
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
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
public class GenerateSettlementStatementReportCommandHandler implements GenerateSettlementStatementReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementStatementReportCommandHandler.class);

    private final JdbcTemplate jdbcTemplate;

    private static final int MAX_REPORT_ROWS = 20000;

    @Autowired
    public GenerateSettlementStatementReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dfspId", input.fspId());
        params.put("dfspName", input.dfspName());
        params.put("startDate", input.startDate().toString());
        params.put("endDate", input.endDate().toString());
        params.put("currencyId", input.currencyId());
        params.put("timezoneoffset", input.timeZoneOffset());

        String reportQuery = """
                  SELECT Count(createdDate) As rowCount FROM (
                    SELECT
                      tp.createdDate AS createdDate,
                      ROUND(CASE WHEN tp.amount < 0 THEN -tp.amount END, 2) AS fundsIn,
                      ROUND(CASE WHEN tp.amount > 0 THEN  tp.amount END, 2) AS fundsOut,
                      ROUND(ppc.value, 2) AS balance,
                      '-' AS ndcPercent,
                      NULL AS ndc
                    FROM participant p
                    INNER JOIN participantCurrency pc ON p.participantId = pc.participantId
                    INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
                    INNER JOIN transferParticipant tp ON tp.participantCurrencyId = pc.participantCurrencyId
                    LEFT JOIN transferStateChange tscOut
                      ON tp.transferId = tscOut.transferId
                     AND tscOut.transferStateChangeId = (
                        SELECT MAX(transferStateChangeId)
                        FROM transferStateChange tscOut1
                        WHERE tscOut1.transferId = tp.transferId
                          AND tscOut1.transferStateId IN ('RESERVED', 'ABORTED_REJECTED')
                     ) 
                    LEFT JOIN transferStateChange tscIn
                      ON tp.transferId = tscIn.transferId
                     AND tscIn.transferStateChangeId = (
                        SELECT MAX(transferStateChangeId)
                        FROM transferStateChange tscIn1
                        WHERE tscIn1.transferId = tp.transferId
                          AND tscIn1.transferStateId IN ('COMMITTED', 'ABORTED_REJECTED')
                     ) 
                    INNER JOIN participantPosition pp ON pp.participantCurrencyId = pc.participantCurrencyId
                    INNER JOIN participantPositionChange ppc ON ppc.participantPositionId = pp.participantPositionId 
                    LEFT JOIN transferExtension tex ON tex.transferId = tp.transferId
                
                    WHERE (? ='All' OR p.name = ?) AND p.name != 'Hub'
                					AND (tscIn.transferStateChangeId = ppc.transferStateChangeId OR tscOut.transferStateChangeId = ppc.transferStateChangeId)
                					AND tex.transferExtensionId = (SELECT MAX(transferExtensionId) FROM transferExtension tex WHERE tex.transferId = tp.transferId AND tex.key = 'externalReference')
                					AND (tp.createdDate BETWEEN (CASE WHEN SUBSTRING(?,1,1) = '-' THEN\s
                					                CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE
                					                CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END)\s
                					                AND\s
                					                (CASE WHEN SUBSTRING(?,1,1) = '-' THEN\s
                					                CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE
                					                CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END))
                								 AND (? = 'All' OR pc.currencyId= ?)
                
                    GROUP BY
                      tp.createdDate, p.participantId, p.name, tscIn.reason, tscOut.reason,
                      tp.amount, ppc.value, pc.currencyId, pc.participantCurrencyId
                
                    UNION ALL
                
                    SELECT
                      pl.createdDate AS createdDate,
                      NULL AS fundsIn,
                      NULL AS fundsOut,
                      NULL AS balance,
                      CASE
                        WHEN ndc_percent.ndcPercent IS NULL OR ndc_percent.ndcPercent = 0
                          THEN '-'
                        ELSE CONCAT(ROUND(IFNULL(ndc_percent.ndcPercent,0),0), '%')
                      END AS ndcPercent,
                      IFNULL(ROUND(IFNULL(pl.value,0),2),0) AS ndc
                    FROM participant p
                    INNER JOIN participantCurrency pc ON p.participantId = pc.participantId
                    INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId 
                    LEFT JOIN participantLimit pl ON pl.participantCurrencyId = pc.participantCurrencyId
                    LEFT JOIN (
                      SELECT * FROM (
                        SELECT participant_name AS dfspCode, currency, ndc_percent AS ndcPercent, FROM_UNIXTIME(updated_date) AS updatedDate
                        FROM operation_portal.tbl_participant_ndc
                        UNION
                        SELECT participant_name AS dfspCode, currency, ndc_percent AS ndcPercent, FROM_UNIXTIME(updated_date) AS updatedDate
                        FROM operation_portal.tbl_participant_ndc_history
                      ) Q
                    ) ndc_percent
                      ON ndc_percent.dfspCode = p.name
                     AND ndc_percent.currency = pc.currencyId
                     AND ABS(TIMESTAMPDIFF(SECOND, ndc_percent.updatedDate, pl.createdDate)) <= 2 
                    LEFT JOIN operation_portal.tbl_approval_request ar
                      ON ar.participant_name = p.name
                     AND ar.participant_currency = pc.currencyId
                     AND ABS(TIMESTAMPDIFF(SECOND, FROM_UNIXTIME(ar.updated_date), pl.createdDate)) <= 2
                
                   WHERE (? ='All' OR p.name = ?)  AND p.name != 'Hub'
                					AND (pl.createdDate BETWEEN (CASE WHEN SUBSTRING(?,1,1) = '-' THEN\s
                					          CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE
                					          CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END)\s
                					          AND\s
                					          (CASE WHEN SUBSTRING(?,1,1) = '-' THEN\s
                					          CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE
                					          CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END))
                						 AND (? = 'All' OR pc.currencyId= ?)\s
                  ) Q
                
                WHERE NOT (
                    COALESCE(NULLIF(fundsIn, ''), 0) = 0
                AND COALESCE(NULLIF(fundsOut, ''), 0) = 0
                AND COALESCE(NULLIF(balance, ''), 0) = 0
                AND (ndcPercent IS NULL OR ndcPercent = '' OR ndcPercent = '-' OR ndcPercent = 0)
                AND COALESCE(NULLIF(ndc, ''), 0) = 0
                )
                
                """;

        String countQuery = "SELECT * FROM (" + reportQuery + ") x";

        Integer rowCount = jdbcTemplate.queryForObject(
                countQuery,
                new Object[]{
                        input.fspId(),input.fspId(),
                        input.timeZoneOffset(), input.startDate(), input.timeZoneOffset(), input.timeZoneOffset(),
                        input.startDate(), input.timeZoneOffset(), input.timeZoneOffset(),
                        input.timeZoneOffset(), input.endDate(), input.timeZoneOffset(), input.timeZoneOffset(),
                        input.endDate(), input.timeZoneOffset(), input.timeZoneOffset(),
                        input.currencyId(), input.currencyId(),
                        input.fspId(),input.fspId(),
                        input.timeZoneOffset(), input.startDate(), input.timeZoneOffset(), input.timeZoneOffset(),
                        input.startDate(), input.timeZoneOffset(), input.timeZoneOffset(),
                        input.timeZoneOffset(), input.endDate(), input.timeZoneOffset(), input.timeZoneOffset(),
                        input.endDate(), input.timeZoneOffset(), input.timeZoneOffset(),
                        input.currencyId(), input.currencyId(),
                        },
                Integer.class);

        if (rowCount != null && rowCount > MAX_REPORT_ROWS) {

            throw new ReportException(ReportErrors.REPORT_MAXIMUM_LIMIT_EXCEPTION.format(rowCount, MAX_REPORT_ROWS));
        }

        InputStream jrxmlStream =
                this.getClass().getClassLoader()
                .getResourceAsStream(
                        "com/thitsaworks/operation_portal/reporting/report/report/settlementStatementReport.jrxml");

        try (Connection conn = this.jdbcTemplate.getDataSource()
                                                .getConnection()) {

            DatabaseMetaData md = conn.getMetaData();
            LOG.info("URL={} | user={} | driver={} {}",
                     md.getURL(),
                     md.getUserName(),
                     md.getDriverName(),
                     md.getDriverVersion());

            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("settlementStatementReport");
            JasperReport settlementStatementReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(settlementStatementReport, params,
                                                                   conn);

            if (jasperPrint.getPages() == null || jasperPrint.getPages()
                                                             .isEmpty()) {

                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            byte[] rptBytes = new byte[0];

            if (input.filetype()
                     .equalsIgnoreCase("xlsx")) {

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
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

            } else {

                throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
            }

            return new Output(rptBytes);

        } catch (ReportException e) {

            throw e;

        } catch (Exception e) {

            LOG.error("Error : [{}]", e.getMessage());
            throw new ReportException(ReportErrors.REPORT_FAILURE_EXCEPTION);
        }
    }

}
