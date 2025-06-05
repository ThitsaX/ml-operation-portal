package com.thitsa.dfsp_portal.usecase;

import com.thitsa.dfsp_portal.datasource.persistence.CentralLedgerDbSettings;
import com.thitsa.dfsp_portal.usecase.central_ledger.GetHubCurrencies;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {
        CentralLedgerUseCaseConfiguration.class, CentralLedgerDbSettings.class})
public class GetHubCurrenciesUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetHubCurrenciesUnitTest.class);

    @Autowired
    private GetHubCurrencies getHubCurrencies;

    @Test
    public void test_getHubCurrenciesSuccessfully() throws Exception {

        var output = this.getHubCurrencies.execute(new GetHubCurrencies.Input());

        if (output.getCurrencyDataList() != null && output.getCurrencyDataList().size() > 0) {
            for (var obj : output.getCurrencyDataList())
                LOG.info(obj.getCurrency());
        } else {
            LOG.info("No record");
        }
    }
}
