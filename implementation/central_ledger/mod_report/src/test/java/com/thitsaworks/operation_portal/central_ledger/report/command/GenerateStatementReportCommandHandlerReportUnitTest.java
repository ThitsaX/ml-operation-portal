package com.thitsaworks.operation_portal.central_ledger.report.command;

import com.thitsaworks.operation_portal.central_ledger.report.ReportConfiguration;
import com.thitsaworks.operation_portal.central_ledger.report.TestSettings;
import com.thitsaworks.operation_portal.central_ledger.report.domain.impl.GenerateStatementReportCommandHandler;
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
public class GenerateStatementReportCommandHandlerReportUnitTest {

    @Autowired
    private GenerateStatementReportCommandHandler dfspDailyTransactionTrendSummary;

    @Test
    public void testGenerateReportSuccessfully() throws IOException, JRException, SQLException {

        FileOutputStream foutdetail = new FileOutputStream(new File("C:\\Workspace\\Development\\settlement_detail_report.xlsx"));

        //dfspDailyTransactionTrendSummary.generate("mmdokdollar",2022/07/26, "2022-10-26", "11110000",".xlsx", foutdetail);
    }
}
