package com.thitsaworks.operation_portal.central_ledger.report.command;

import com.thitsaworks.operation_portal.central_ledger.report.ReportConfiguration;
import com.thitsaworks.operation_portal.central_ledger.report.TestSettings;
import com.thitsaworks.operation_portal.central_ledger.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.central_ledger.report.domain.impl.GenerateSettlementReportCommandHandler;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

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
