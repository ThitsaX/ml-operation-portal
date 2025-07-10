package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.common.type.MyanmarZoneId;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
            var fromDate = this.convertInstantToDate(input.fromDate());
            var toDate = this.convertInstantToDate(input.toDate());

            params.put("timezoneoffset", input.timezoneoffset());
            params.put("fromDate", fromDate);
            params.put("toDate", toDate);
            params.put("realmId", String.valueOf(input.realmId()));

            LOG.info("fromDate : [{}], toDate : [{}]", fromDate, toDate);

            InputStream jrxmlStream = getClass().getClassLoader()
                                                .getResourceAsStream(
                                                    "com/thitsaworks/operation_portal/reporting/report/report/ReportTesting.jrxml");
            JasperDesign design = JRXmlLoader.load(jrxmlStream);
            design.setName("ReportTesting");
            JasperReport jasperReport = JasperCompileManager.compileReport(design);


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params,
                                                                   conn);

            byte[] rptBytes = new byte[0];

            JRPdfExporter pdfExporter = new JRPdfExporter();
            ByteArrayOutputStream pdfReport = new ByteArrayOutputStream();
            pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReport));
            pdfExporter.exportReport();
            rptBytes = pdfReport.toByteArray();

            return new Output(rptBytes);

        } catch (Exception e) {

            LOG.info("Error : [{}]", e.getMessage());
            throw new ReportException(ReportErrors.REPORT_FAILURE_EXCEPTION);
        }
    }

    private Date convertInstantToDate(Instant instant) {

        ZoneId zoneId = ZoneId.of(MyanmarZoneId.ZONE_ID);

        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        return Date.from(zonedDateTime.toInstant());

    }

}
