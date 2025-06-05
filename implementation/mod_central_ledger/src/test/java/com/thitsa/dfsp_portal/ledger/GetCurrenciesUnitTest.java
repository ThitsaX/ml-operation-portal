package com.thitsa.dfsp_portal.ledger;

import com.thitsa.dfsp_portal.ledger.persistence.CentralLedgerDBSetting;
import com.thitsa.dfsp_portal.ledger.query.GetCurrencies;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerConfiguration.class, CentralLedgerReadDbConfiguration.class, CentralLedgerDBSetting.class})
public class GetCurrenciesUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetCurrenciesUnitTest.class);

    @Autowired
    private GetCurrencies getCurrencies;

    @Test
    public void test_getCurrenciesSuccessfully() throws Exception {

        GetCurrencies.Output output = this.getCurrencies.execute(new GetCurrencies.Input());

        if (output.getCurrencyDataList() != null && output.getCurrencyDataList().size() > 0) {

            for (var obj : output.getCurrencyDataList())
                LOG.info(obj.getCurrency()
                );
        } else {
            LOG.info("No record");
        }

    }
}
