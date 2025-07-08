package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.CentralLedgerConfiguration;
import com.thitsaworks.operation_portal.reporting.central_ledger.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CentralLedgerConfiguration.class, TestSettings.class})
public class GetCurrenciesUnitTest{

    private static final Logger LOG = LoggerFactory.getLogger(GetCurrenciesUnitTest.class);

    @Autowired
    private GetCurrenciesQuery getCurrenciesQuery;

    @Test
    public void test_getCurrenciesSuccessfully() throws Exception {

        GetCurrenciesQuery.Output output = this.getCurrenciesQuery.execute(new GetCurrenciesQuery.Input());

        if (output.getCurrencyDataList() != null && output.getCurrencyDataList().size() > 0) {

            for (var obj : output.getCurrencyDataList()) {
                LOG.info(obj.getCurrency()
                        );
            }
        } else {
            LOG.info("No record");
        }

    }

}
