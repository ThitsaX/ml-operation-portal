package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateAuditReportCommandHandler implements GenerateAuditReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateAuditReportCommandHandler.class);

    private final JdbcTemplate jdbcTemplate;

    public GenerateAuditReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Core.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public Output execute(Input input) throws ReportException {

        Map<String, Object> params = new HashMap<String, Object>();

        try (Connection conn = this.jdbcTemplate.getDataSource()
                                                .getConnection()) {

            var timeOffset = input.timezoneoffset();


            params.put("timezoneoffset", timeOffset);
            params.put("fromDate", input.fromDate().getEpochSecond());
            params.put("toDate", input.toDate().getEpochSecond());

            if (input.realmId() != null) {
                params.put("realmId", input.realmId());
            }

            if (input.userId() != null) {
                params.put("userId", input.userId());
            }

            if (input.actionId() != null) {
                params.put("actionId", input.actionId());
            }

            params.put("grantedActionList", input.grantedActionList());


            InputStream jrxmlStream = getClass().getClassLoader()
                                                .getResourceAsStream(
                                                    "com/thitsaworks/operation_portal/reporting/report/report/auditReport.jrxml");

            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("auditReport");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,
                                                                   conn);

            byte[] rptBytes = new byte[0];

            if (input.fileType()
                     .equalsIgnoreCase("xlsx")) {

                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
                xlsxExporter.exportReport();
                rptBytes = xlsReport.toByteArray();
                
            } else if (input.fileType()
                            .equalsIgnoreCase("csv")) {

                JRCsvExporter csvExporter = new JRCsvExporter();
                ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
                csvExporter.exportReport();

                String csvContent = csvReport.toString(StandardCharsets.UTF_8);
                csvContent = "\n" + csvContent;

                rptBytes = csvContent.getBytes(StandardCharsets.UTF_8);
            }

            return new Output(rptBytes);

        } catch (Exception e) {

            LOG.info("Error : [{}]", e.getMessage());
            throw new ReportException(ReportErrors.AUDIT_REPORT_FAILURE_EXCEPTION);
        }
    }


}
