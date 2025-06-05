package com.thitsa.dfsp_portal.ledger;

import com.thitsa.dfsp_portal.ledger.persistence.CentralLedgerDBSetting;
import com.thitsa.dfsp_portal.ledger.query.GetFinancialData;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerConfiguration.class, CentralLedgerReadDbConfiguration.class, CentralLedgerDBSetting.class})
public class GetFinancialDataUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetFinancialDataUnitTest.class);

    @Autowired
    private GetFinancialData getFinancialData;

    @Test
    public void test_getFinancialDataSuccessfully() throws Exception {

        GetFinancialData.Output output = this.getFinancialData.execute(
                new GetFinancialData.Input("wallet2"));

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
