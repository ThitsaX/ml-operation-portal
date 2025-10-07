package com.thitsaworks.operation_portal.core.hub_services.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.core.hub_services.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import com.thitsaworks.operation_portal.core.hub_services.data.TransferDetailData;
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
public class GetTranferDetailQueryUT extends BaseVaultSetUpTest {

    private static final Logger logger = LoggerFactory.getLogger(GetTranferDetailQueryUT.class);

    @Autowired
    private GetTransferDetailQuery getTransferDetailQuery;

    @Test
    public void testGetTransferDetail() throws HubServicesException {

        TransferDetailData output = getTransferDetailQuery.execute("01K18F29HYWRH2P8PYX4SZ7MDQ","0630");

        ObjectMapper mapper = new ObjectMapper();

        if (output == null) {return;}

        try {

            logger.info("Transfer Details : {}", mapper.writeValueAsString(output));

        } catch (Exception e) {

            logger.error("Error serializing transferDetailData", e);
        }

    }

}