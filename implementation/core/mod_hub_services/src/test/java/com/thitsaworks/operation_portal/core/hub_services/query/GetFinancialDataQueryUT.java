package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubServicesConfiguration.class, TestSettings.class})
public class GetFinancialDataQueryUT extends BaseVaultSetUpTest {
    private static final Logger log = LoggerFactory.getLogger(GetFinancialDataQueryUT.class);

//    @Autowired
//    private GetFinancialDataQuery getFinancialDataQuery;
//
//    @Test
//    public void testExecute() {
//        GetFinancialDataQuery.Input input = new GetFinancialDataQuery.Input("fspID123");
//        GetFinancialDataQuery.Output output = getFinancialDataQuery.execute(input);
//
//        log.info("Financial data retrieved successfully: {}", output.getFinancialData());
//    }

}

