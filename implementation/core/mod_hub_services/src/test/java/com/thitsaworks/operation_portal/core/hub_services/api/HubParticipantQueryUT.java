package com.thitsaworks.operation_portal.core.hub_services.api;

import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import com.thitsaworks.operation_portal.core.hub_services.data.HubParticipantData;
import com.thitsaworks.operation_portal.core.hub_services.query.HubParticipantQuery;
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
public class HubParticipantQueryUT {

    private static final Logger LOG = LoggerFactory.getLogger(HubParticipantQueryUT.class);

    @Autowired
    private HubParticipantQuery hubParticipantQuery;

    @Test
    public void getByNameHubParticipants() throws Exception {

        List<HubParticipantData> hubParticipantDataList = hubParticipantQuery.getParticipantList();

        for (HubParticipantData hubParticipantData : hubParticipantDataList) {
            LOG.info("Hub Participant : {}", hubParticipantData);
        }

    }

    @Test
    public void getByNameHubParticipantByName() throws Exception {

        String participantName = "hub5";

        HubParticipantData hubParticipantData = hubParticipantQuery.getByName(participantName);

        LOG.info("Hub Participant : {}", hubParticipantData);

    }

}
