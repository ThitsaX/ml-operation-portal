package com.thitsaworks.operation_portal.reporting.report.command;

import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import com.thitsaworks.operation_portal.reporting.report.TestSettings;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ReportConfiguration.class, TestSettings.class})
public class DFSPGenerateSettlementDetailReportCommandHandlerUnitTest {

    @Autowired
    private GenerateSettlementDetailReportCommand generateSettlementDetailReportCommand;

    @Test
    public void testGenerateReportSuccessfully() throws Exception {

        GenerateSettlementDetailReportCommand.Output output = this.generateSettlementDetailReportCommand.execute(new GenerateSettlementDetailReportCommand.Input("1",
                                                                                                           "all",
                                                                                                           "All",
                                                                                                           ".xlsx",
                                                                                                           "0630"));

        System.out.println(Arrays.toString(output.settlementDetailRptByte()));
    }

}
