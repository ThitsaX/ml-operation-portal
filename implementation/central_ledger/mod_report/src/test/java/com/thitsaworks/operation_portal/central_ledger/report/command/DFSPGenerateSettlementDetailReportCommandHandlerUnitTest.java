package com.thitsaworks.operation_portal.central_ledger.report.command;

import com.thitsaworks.operation_portal.central_ledger.report.ReportConfiguration;
import com.thitsaworks.operation_portal.central_ledger.report.TestSettings;
import com.thitsaworks.operation_portal.central_ledger.report.domain.GenerateSettlementDetailReportCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReportConfiguration.class, TestSettings.class})
public class DFSPGenerateSettlementDetailReportCommandHandlerUnitTest {

    @Autowired
    private GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand;

    @Test
    public void testGenerateReportSuccessfully() throws Exception {

//        FileOutputStream fout = new FileOutputStream(new File(
//                "C:\\Workspace\\Development\\settlement_detail_report.xlsx"));

        this.generateSettlementDetailReportCommand.execute(new GenerateSettlementDetailReportCommand.Input("1",
                                                                                                           "mmdokdollar",
                                                                                                           ".xlsx",
                                                                                                           "0630"));
    }

}
