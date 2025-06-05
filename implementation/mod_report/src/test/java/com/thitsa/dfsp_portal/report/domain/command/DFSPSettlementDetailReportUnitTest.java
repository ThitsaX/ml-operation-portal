package com.thitsa.dfsp_portal.report.domain.command;

import com.thitsa.dfsp_portal.report.ReportConfiguration;
import com.thitsa.dfsp_portal.report.domain.SettlementDetailReport;
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
public class DFSPSettlementDetailReportUnitTest {

    @Autowired
    private SettlementDetailReport dfspSettlementDetailReport;

    @Test
    public void testGenerateReportSuccessfully() throws IOException, JRException, SQLException {

        FileOutputStream fout = new FileOutputStream(new File(
                "C:\\Workspace\\Development\\settlement_detail_report.xlsx"));

        dfspSettlementDetailReport.generate("1", "mmdokdollar", ".xlsx", "0630");
    }

}
