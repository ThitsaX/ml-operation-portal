package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubServicesConfiguration.class, TestSettings.class})
public class ParticipantAccountBalanceUT {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantAccountBalanceUT.class);

    @Test
    public void getParticipantAccountBalance() throws Exception {

        var participantHubClient = new ParticipantHubClient(new HubServicesConfiguration.Settings(
                "http://localhost:52202",
                "http://localhost:52721/v2/"));

        GetParticipantAccountBalance.Request request = new GetParticipantAccountBalance.Request("wallet1");

        var output = participantHubClient.getParticipantAccountBalance(request);

        if (output != null) {

            LOG.info("Output : <{}>", new ObjectMapper().writeValueAsString(output));

        } else {

            LOG.info("Output : {} ", "");
        }

    }

}
