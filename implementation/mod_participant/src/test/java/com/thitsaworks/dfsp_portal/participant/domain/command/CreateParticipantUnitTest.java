package com.thitsaworks.dfsp_portal.participant.domain.command;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import com.thitsaworks.dfsp_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.type.DfspCode;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class CreateParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantUnitTest.class);

    @Autowired
    private CreateParticipant createParticipant;

    @Test
    public void test_createParticipantSuccessfully() throws Exception {

        this.createParticipant.execute(
                new CreateParticipant.Input("Vision Fund",
                        new DfspCode("abcdefg"),"abc",
                        "Yankin Township, Yangon.",
                        new Mobile("+959250661838")));
    }

}
