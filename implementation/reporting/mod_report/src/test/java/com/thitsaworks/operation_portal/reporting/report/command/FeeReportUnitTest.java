package com.thitsaworks.operation_portal.reporting.report.command;

import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import com.thitsaworks.operation_portal.reporting.report.TestSettings;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSettlementReportCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@ContextConfiguration(classes = {
        ReportConfiguration.class, TestSettings.class})
public class FeeReportUnitTest {

    @Autowired
    private GenerateFeeSettlementReportCommand generateFeeSettlementReportCommand;

    @Test
    public void testGenerateReportSuccessfully() throws Exception {

        FileOutputStream foutdetail = new FileOutputStream(new File("C:\\Workspace\\Development\\abc.xlsx"));

        Instant startDate = LocalDate.of(2022, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        Instant endDate = LocalDate.of(2022, 9, 9).atStartOfDay(ZoneId.systemDefault()).toInstant();

        generateFeeSettlementReportCommand.execute(new GenerateFeeSettlementReportCommand.Input(startDate,
                                                                                                endDate,
                                                                                                "all",
                                                                                                "all",
                                                                                                "all",
                                                                                                "0630",
                                                                                                "xlsx"));
    }

}
