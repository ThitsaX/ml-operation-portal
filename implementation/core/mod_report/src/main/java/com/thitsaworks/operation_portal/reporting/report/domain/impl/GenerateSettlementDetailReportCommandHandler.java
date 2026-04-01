package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
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
import net.sf.jasperreports.export.SimpleCsvExporterConfiguration;
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
public class GenerateSettlementDetailReportCommandHandler
    implements GenerateSettlementDetailReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateSettlementDetailReportCommandHandler.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenerateSettlementDetailReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("settlementId", input.settlementId());
        params.put("fspId", input.fspId());
        params.put("dfspName", input.dfspName());
        params.put("timezoneoffset", input.timezoneOffset());
        params.put("report", input.fileType());
        params.put("offset", input.offset() == null ? 0 : input.offset());
        params.put("limit", input.limit());

        InputStream jrxmlStream = this
                                      .getClass()
                                      .getClassLoader()
                                      .getResourceAsStream(
                                          "com/thitsaworks/operation_portal/reporting/report/report/dfspSettlementDetailReport.jrxml");

        try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()) {

            DatabaseMetaData md = conn.getMetaData();
            LOG.info(
                "URL={} | user={} | driver={} {}", md.getURL(), md.getUserName(),
                md.getDriverName(), md.getDriverVersion());

            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("dfspSettlementDetailReport");
            JasperReport detailReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(detailReport, params, conn);

            if (jasperPrint.getPages() == null || jasperPrint.getPages().isEmpty()) {

                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            byte[] rptBytes = new byte[0];

            if (input.fileType().equalsIgnoreCase("xlsx")) {

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();

            } else if (input.fileType().equalsIgnoreCase("csv")) {

                JRCsvExporter csvExporter = new JRCsvExporter();
                ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));

                SimpleCsvExporterConfiguration config = new SimpleCsvExporterConfiguration();

                config.setForceFieldEnclosure(false);
                csvExporter.setConfiguration(config);

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
            throw new ReportException(ReportErrors.SETTLEMENT_DETAIL_REPORT_FAILURE_EXCEPTION);

        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.jdbcTemplate.queryForObject(
            """
                SELECT COUNT(DISTINCT tF.transferId) AS rowCount
                FROM transferFulfilment tF
                INNER JOIN transfer t ON t.transferId = tF.transferId
                INNER JOIN transferParticipant tPPayer ON tPPayer.transferId = tF.transferId
                    AND tPPayer.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYER_DFSP')
                INNER JOIN participantCurrency pCPayer ON pCPayer.participantCurrencyId = tPPayer.participantCurrencyId
                INNER JOIN participant pPayer ON pPayer.participantId = pCPayer.participantId
                INNER JOIN transferParticipant tPPayee ON tPPayee.transferId = tF.transferId
                    AND tPPayee.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYEE_DFSP')
                INNER JOIN participantCurrency pCPayee ON pCPayee.participantCurrencyId = tPPayee.participantCurrencyId
                INNER JOIN participant pPayee ON pPayee.participantId = pCPayee.participantId
                INNER JOIN settlementSettlementWindow sSW ON tF.settlementWindowId = sSW.settlementWindowId
                INNER JOIN settlement s ON sSW.settlementId = s.settlementId
                WHERE tF.isValid
                  AND s.settlementId = ?
                  AND (pPayee.name = ? OR pPayer.name = ?)
                """, new Object[]{
                input.settlementId(),
                input.fspId(),
                input.fspId()}, Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

}
