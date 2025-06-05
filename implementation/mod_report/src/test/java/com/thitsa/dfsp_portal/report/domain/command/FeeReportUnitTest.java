package com.thitsa.dfsp_portal.report.domain.command;

import com.thitsa.dfsp_portal.report.ReportConfiguration;
import com.thitsa.dfsp_portal.report.domain.FeeSettlementReport;
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
import java.text.ParseException;

@ContextConfiguration(classes = {
        ReportConfiguration.class, CentralLedgerReadDbConfiguration.class, ReportDbSettings.class})
public class FeeReportUnitTest {

    @Autowired
    private FeeSettlementReport feeSettlementReport;

    @Test
    public void testGenerateReportSuccessfully() throws IOException, JRException, SQLException, ParseException {

        FileOutputStream foutdetail = new FileOutputStream(new File("C:\\Workspace\\Development\\abc.xlsx"));

        feeSettlementReport.generate("2022-09-01","2022-09-09", "all", "all","all","0630","xlsx");
    }
}
