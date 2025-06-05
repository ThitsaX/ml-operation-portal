package com.thitsa.dfsp_portal.report.domain.command;

import com.thitsa.dfsp_portal.report.ReportConfiguration;
import com.thitsa.dfsp_portal.report.domain.SettlementStatement;
import com.thitsa.dfsp_portal.report.domain.persistence.ReportDbSettings;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

@ContextConfiguration(classes = {
        ReportConfiguration.class, CentralLedgerReadDbConfiguration.class, ReportDbSettings.class})
public class SettlementStatementReportUnitTest {

    @Autowired
    private SettlementStatement dfspDailyTransactionTrendSummary;

    @Test
    public void testGenerateReportSuccessfully() throws IOException, JRException, SQLException {

        FileOutputStream foutdetail = new FileOutputStream(new File("C:\\Workspace\\Development\\settlement_detail_report.xlsx"));

        //dfspDailyTransactionTrendSummary.generate("mmdokdollar",2022/07/26, "2022-10-26", "11110000",".xlsx", foutdetail);
    }
}
