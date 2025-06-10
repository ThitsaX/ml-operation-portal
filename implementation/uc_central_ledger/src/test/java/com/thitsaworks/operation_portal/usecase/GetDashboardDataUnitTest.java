package com.thitsaworks.operation_portal.usecase;

import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerDbSettings;
import com.thitsaworks.operation_portal.usecase.central_ledger.GetDashboardData;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerUseCaseConfiguration.class, CentralLedgerDbSettings.class})
public class GetDashboardDataUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetDashboardDataUnitTest.class);

    @Autowired
    private GetDashboardData getDashboardData;

    @Test
    public void test_getFinancialInfoSuccessfully() throws Exception {

        var output =
                this.getDashboardData.execute(new GetDashboardData.Input(new ParticipantUserId(392628367895068672L)));

        if (output.getFinancialData() != null && !output.getFinancialData().isEmpty()) {

            for (var financialData : output.getFinancialData()) {

                LOG.info(financialData.getDfspName() + " , " + financialData.getCurrency() + " , " +
                        financialData.getBalance() + " , " + financialData.getCurrentPosition() + " , " +
                        financialData.getNdc() + " , " + financialData.getNdcUsed());
            }
        } else {
            LOG.info("No record");
        }
    }

}
