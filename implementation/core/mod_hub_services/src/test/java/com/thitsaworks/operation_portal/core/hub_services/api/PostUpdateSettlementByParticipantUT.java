package com.thitsaworks.operation_portal.core.hub_services.api;

import com.thitsaworks.operation_portal.component.fspiop.model.Currency;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import com.thitsaworks.operation_portal.core.hub_services.BaseVaultSetUpTest;
import com.thitsaworks.operation_portal.core.hub_services.HubServicesConfiguration;
import com.thitsaworks.operation_portal.core.hub_services.ParticipantHubClient;
import com.thitsaworks.operation_portal.core.hub_services.TestSettings;
import com.thitsaworks.operation_portal.core.hub_services.exception.HubServicesException;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HubServicesConfiguration.class, TestSettings.class})
public class PostUpdateSettlementByParticipantUT extends BaseVaultSetUpTest {

    private static final Logger logger = LoggerFactory.getLogger(PostUpdateSettlementByParticipantUT.class);

    @Autowired
    private ParticipantHubClient participantHubClient;

    @Test
    public void test() throws HubServicesException {

        PostParticipantBalance.Request request =
                new PostParticipantBalance.Request(UUID.randomUUID().toString(),
                                                   SettlementAction.recordFundsOutPrepareReserve.toString(),
                                                   "Business Operations Portal settlement ID 34 finalization report processing",
                                                   "",
                                                   new Money().currency(Currency.USD).amount("1000"),
                                                   new ExtensionList());

        var output = this.participantHubClient.postParticipantBalance("wallet1", "29", request);
        logger.info("Output: {}", output);
    }

}

