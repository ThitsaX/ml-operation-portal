package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerDbSettings;
import com.thitsaworks.operation_portal.usecase.central_ledger.GenerateSettlementReport;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerUseCaseConfiguration.class, CentralLedgerDbSettings.class})
public class GenerateSettlementReportUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementReportUnitTest.class);

    @Autowired
    private GenerateSettlementReport generateSettlementReport;

    @Test
    public void test_generateSettlementReportSuccessfully() throws Exception {


    }

}
