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
public class GetTranferQueryIT extends BaseVaultSetUpTest {

    private static final Logger logger = LoggerFactory.getLogger(GetTranferQueryIT.class);

    @Autowired
    private GetTransfersQuery getTransfersQuery;

    @Test
    public void testGetTransfers() throws HubServicesException {

        var input = new GetTransfersQuery.Input(
            "2025-07-01T00:00:00Z",
            "2025-08-12T23:59:59Z",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null

        );

        var output = getTransfersQuery.execute(input);

        logger.info("Transfers retrieved: {}",
                    output.getTransferInfoList()
                          .size());
    }

}