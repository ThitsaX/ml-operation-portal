package com.thitsaworks.operation_portal.core.hub_services.query;

import com.thitsaworks.operation_portal.core.hub_services.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import com.thitsaworks.operation_portal.core.hub_services.api.PostUpdateSettlementByParticipant;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAmount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubServicesConfiguration.class, TestSettings.class})
public class PostUpdateSettlementByiParticipantIT extends BaseVaultSetUpTest {

    private static final Logger logger = LoggerFactory.getLogger(PostUpdateSettlementByiParticipantIT.class);

    @Autowired
    private ParticipantHubClient participantHubClient;

    @Test
    public void test() throws HubServicesException, ConnectException {

        PostUpdateSettlementByParticipant.Request request =
                new PostUpdateSettlementByParticipant.Request(UUID.randomUUID().toString(),
                                                              SettlementAction.recordFundsOutPrepareReserve.toString(),
                                                              "Business Operations Portal settlement ID 34 finalization report processing",
                                                              "",
                                                              new SettlementAmount(
                                                                      BigDecimal.valueOf(1000), "USD"));

        var output = this.participantHubClient.postUpdateSettlementByParticipant("wallet1", 29, request);
        logger.info("Output: {}", output);
    }

}

