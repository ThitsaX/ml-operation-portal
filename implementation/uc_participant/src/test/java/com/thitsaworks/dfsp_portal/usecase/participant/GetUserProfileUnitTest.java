package com.thitsaworks.dfsp_portal.usecase.participant;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.dfsp_portal.usecase.ParticipantUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetUserProfileUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserProfileUnitTest.class);

    @Autowired
    private GetUserProfile GetUserProfile;

    @Test
    public void test_getUserProfileSuccessfully() throws Exception {

        GetUserProfile.Output output =
                this.GetUserProfile.execute(new GetUserProfile.Input(new ParticipantUserId(407952833558642688L)));

        if (output != null) {

            LOG.info(output.getParticipantUserId().getId() + " , " + output.getName() + " , " +
                    output.getEmail().getValue() + " , " + output.getFirstName() + " , " + output.getLastName() +
                    " , " + output.getJobTitle());
        } else {
            LOG.info("No record");
        }
    }

}
