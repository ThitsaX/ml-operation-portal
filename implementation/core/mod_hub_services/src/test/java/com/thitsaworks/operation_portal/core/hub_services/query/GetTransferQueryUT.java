package com.thitsaworks.operation_portal.core.hub_services.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.core.hub_services.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferData;
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
public class GetTransferQueryUT extends BaseVaultSetUpTest {

    private static final Logger logger = LoggerFactory.getLogger(GetTransferQueryUT.class);

    @Autowired
    private GetTransfersQuery getTransfersQuery;

    @Test
    public void testGetTransfers() throws HubServicesException {

        var input = new GetTransfersQuery.Input(
                "2025-07-24T06:00:00Z", "2025-07-29T05:59:59Z",
            null,
            "wallet1",
            "wallet1",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
                "0900"

        );

        var output = getTransfersQuery.execute(input);

        ObjectMapper mapper = new ObjectMapper();

        if (output.getTransferInfoList().isEmpty()) {return;}

        for (TransferData transferData : output.getTransferInfoList()) {

            try {

                logger.info("Transfers retrieved: {}", mapper.writeValueAsString(transferData));

            } catch (Exception e) {

                logger.error("Error serializing transferData", e);
            }
        }
    }

}