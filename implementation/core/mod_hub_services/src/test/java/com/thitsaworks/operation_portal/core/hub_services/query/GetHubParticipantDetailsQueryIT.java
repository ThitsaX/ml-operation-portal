package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantDetailData;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubServicesConfiguration.class, TestSettings.class})
public class GetHubParticipantDetailsQueryIT extends BaseVaultSetUpTest {

    private static final Logger logger = LoggerFactory.getLogger(GetHubParticipantDetailsQueryIT.class);

    @Autowired
    private HubParticipantQuery hubParticipantQuery;

    @Test
    public void testGetParticipants() throws HubServicesException {

        List<HubParticipantDetailData> hubParticipantDetailDataList =
                hubParticipantQuery.getHubParticipantDetailDataList();

        for (HubParticipantDetailData hubParticipantDetailData : hubParticipantDetailDataList) {
            logger.info("Hub Participant: {}", hubParticipantDetailData);
        }

    }

}