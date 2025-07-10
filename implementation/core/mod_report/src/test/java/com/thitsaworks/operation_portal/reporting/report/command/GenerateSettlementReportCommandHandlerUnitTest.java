package com.thitsaworks.operation_portal.reporting.report.command;

import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import com.thitsaworks.operation_portal.reporting.report.TestSettings;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.impl.GenerateSettlementReportCommandHandler;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileOutputStream;

@ContextConfiguration(classes = {
        ReportConfiguration.class, TestSettings.class})
public class GenerateSettlementReportCommandHandlerUnitTest extends EnvAwareUnitTest {

    @Autowired
    private GenerateSettlementReportCommandHandler generateSettlementReportCommandHandler;

    @Test
    public void testGenerateReportSuccessfully() throws Exception {

        FileOutputStream fout = new FileOutputStream(new File("C:\\settlement_report.xlsx"));

        generateSettlementReportCommandHandler.execute(new GenerateSettlementReportCommand.Input("mmdokdollar", "1", ".xlsx", "0630"));
    }
}
