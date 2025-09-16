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
public class GetIDTypesQueryUT extends BaseVaultSetUpTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetIDTypesQueryUT.class);

    @Autowired
    private GetIDTypesQuery getIDTypesQuery;

    @Test
    public void testExecute() throws HubServicesException {

        GetIDTypesQuery.Input input = new GetIDTypesQuery.Input();
        GetIDTypesQuery.Output output = this.getIDTypesQuery.execute(input);
        LOGGER.info("ID Types retrieved successfully: {}", output.getIdTypeDataList());
    }

}
