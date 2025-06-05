package com.thitsaworks.dfsp_portal.participant.domain.command;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.component.type.Mobile;
import com.thitsaworks.dfsp_portal.participant.ParticipantConfiguration;
import com.thitsaworks.dfsp_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class ModifyParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantUnitTest.class);

    @Autowired
    private ModifyParticipant modifyParticipant;

    @Test
    public void test_modifyParticipantSuccessfully() throws Exception {

        this.modifyParticipant.execute(
                new ModifyParticipant.Input(new ParticipantId(390908599471210496L),"abc",
                        "Ahlone Township, Yangon.",
                        new Mobile("+959250661838")));
    }

}
