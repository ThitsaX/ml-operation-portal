package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubServicesConfiguration.class, TestSettings.class})
public class GetCurrenciesQueryUT extends BaseVaultSetUpTest {
    private static final Logger logger = LoggerFactory.getLogger(GetCurrenciesQueryUT.class);

    @Autowired
    private GetCurrenciesQuery getCurrenciesQuery;

    @Test
    public void test() throws HubServicesException {
        var input = new GetCurrenciesQuery.Input();
        var output = getCurrenciesQuery.execute(input);
        logger.info("Output: {}", output);
    }
}

