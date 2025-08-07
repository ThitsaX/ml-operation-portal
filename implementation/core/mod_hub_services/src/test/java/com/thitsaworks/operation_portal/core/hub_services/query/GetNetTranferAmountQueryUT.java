package com.thitsaworks.operation_portal.core.hub_services.query;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class GetNetTranferAmountQueryUT extends BaseVaultSetUpTest {

    private static final Logger logger = LoggerFactory.getLogger(GetNetTranferAmountQueryUT.class);

    @Autowired
    private GetNetTransferAmountByWindowIdQuery getTransferDetailQuery;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetTransfers() throws HubServicesException, JsonProcessingException {

        GetNetTransferAmountByWindowIdQuery.Output output = getTransferDetailQuery.execute(new GetNetTransferAmountByWindowIdQuery.Input(25));

        logger.info("Net Transfer Amount: {}", this.objectMapper.writeValueAsString(output));
    }

}