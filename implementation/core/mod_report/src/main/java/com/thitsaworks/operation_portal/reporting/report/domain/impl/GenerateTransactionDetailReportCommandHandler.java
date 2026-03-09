package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateTransactionDetailReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
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
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateTransactionDetailReportCommandHandler
    implements GenerateTransactionDetailReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateTransactionDetailReportCommandHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenerateTransactionDetailReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startDate", input.startDate().toString());
        params.put("endDate", input.endDate().toString());
        params.put("state", input.state());
        params.put("dfspId", input.dfspId());
        params.put("timezoneoffset", input.timeZoneOffset());
        params.put("offset", input.offset() == null ? 0 : input.offset());
        params.put("limit", input.limit());

        LOG.info("Params : {}", params);

        InputStream jrxmlStream = getClass()
                                      .getClassLoader()
                                      .getResourceAsStream(
                                          "com/thitsaworks/operation_portal/reporting/report/report/transactionDetailReport.jrxml");

        try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()) {

            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("transactionDetailReport");
            JasperReport transactionDetailReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                transactionDetailReport, params, conn);

            if (jasperPrint.getPages() == null || jasperPrint.getPages().isEmpty()) {

                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            byte[] rptBytes = new byte[0];

            if (input.filetype().equalsIgnoreCase("xlsx")) {

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
                config.setWrapText(true);
                config.setWhitePageBackground(false);
                config.setIgnoreGraphics(false);

                xlsxExporter.setConfiguration(config);

                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

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
            throw new ReportException(ReportErrors.TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.countTransactionDetailRows(
            input.startDate(), input.endDate(),
            input.state(), input.dfspId(), input.timeZoneOffset());
        return rowCount == null ? 0 : rowCount;
    }

    private Integer countTransactionDetailRows(java.time.Instant startDate,
                                               java.time.Instant endDate,
                                               String state,
                                               String dfspId,
                                               String timeZoneOffset) {

        String reportQuery = """
                WITH bounds_base AS (
                                  SELECT
                                    CASE WHEN SUBSTRING(?,1,1) = '-' THEN\s
                                						    CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE
                                						    CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END AS startUtc,
                                    CASE WHEN SUBSTRING(?,1,1) = '-' THEN\s
                                						    CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00') ELSE
                                						    CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)) ,'+00:00') END AS endUtc
                                ),
                                bounds AS (
                                            SELECT
                                              startUtc,
                                              endUtc,
                                             DATE_ADD(endUtc, INTERVAL 1 MINUTE) AS endUtcPlus1Min,
                                             DATE_ADD(startUtc, INTERVAL -1 MINUTE) AS startUtcMinus1Min
                                            FROM bounds_base
                                          )
            
                                SELECT COUNT(*) AS rowCount
                                FROM transfer t
                                LEFT JOIN transferParticipant tppayer ON t.transferId = tppayer.transferId AND tppayer.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYER_DFSP')
                                LEFT JOIN participantCurrency payercurrency ON payercurrency.participantCurrencyId = tppayer.participantCurrencyId
                                LEFT JOIN participant payer ON payer.participantId = payercurrency.participantId
                                LEFT JOIN transferParticipant tppayee ON t.transferId = tppayee.transferId AND tppayee.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYEE_DFSP')
                                LEFT JOIN participantCurrency payeecurrency ON payeecurrency.participantCurrencyId = tppayee.participantCurrencyId
                                LEFT JOIN participant payee ON payee.participantId = payeecurrency.participantId
                                LEFT JOIN quote q ON q.transactionReferenceId = t.transferId
                                LEFT JOIN quoteResponse qR ON qR.quoteId = q.quoteId
                                LEFT JOIN (
                                			SELECT  t.transferId, t.transferStateId, t.createdDate
                                				FROM transferStateChange t
                                				JOIN (
                                						SELECT transferId, MAX(transferStateChangeId) AS maxId
                                						FROM transferStateChange
                                						JOIN bounds b
                                						WHERE createdDate BETWEEN b.startUtcMinus1Min AND b.endUtcPlus1Min
                                						GROUP BY transferId
                                				) m
                                  				ON m.transferId = t.transferId AND m.maxId = t.transferStateChangeId
                                		) tss ON tss.transferId = t.transferId
                                LEFT JOIN transferState tst ON tst.transferStateId= tss.transferStateId\s
                                LEFT JOIN amountType a ON a.amountTypeId = q.amountTypeId
                                INNER JOIN currency c ON c.currencyId = payercurrency.currencyId\s                                 
                                JOIN bounds b\s
                                WHERE (IFNULL(tss.createdDate,t.createdDate) BETWEEN  b.startUtc AND b.endUtc)
                                	 AND (? ='All' OR tst.enumeration = ?)
                                	  AND ((? ='All')  OR (payee.name = ?  OR payer.name = ?))
            """;

        String countQuery = "SELECT * FROM (" + reportQuery + ") x";

        return jdbcTemplate.queryForObject(
            countQuery, new Object[]{
                timeZoneOffset,
                startDate,
                timeZoneOffset,
                timeZoneOffset,
                startDate,
                timeZoneOffset,
                timeZoneOffset,
                timeZoneOffset,
                endDate,
                timeZoneOffset,
                timeZoneOffset,
                endDate,
                timeZoneOffset,
                timeZoneOffset,
                state,
                state,
                dfspId,
                dfspId,
                dfspId}, Integer.class);

    }

}
