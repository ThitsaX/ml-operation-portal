package com.thitsaworks.dfsp_portal.usecase.participant;

import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.dfsp_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.usecase.ParticipantUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetAllParticipantUsersUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetAllParticipantUsersUnitTest.class);

    @Autowired
    private GetAllParticipantUser getAllParticipantUser;

    @Test
    public void test_getAllUserForParticipantSuccessfully() throws Exception {

        //   var output = this.getAllParticipantUser.execute(
        //    new GetAllParticipantUser.Input(new ParticipantId(390908599471210496L)));

        GetAllParticipantUser.Output output = this.getAllParticipantUser.execute(
                new GetAllParticipantUser.Input(new ParticipantId(392623203375808512L)));

        if (output.getUserInfoList() != null) {
            for (var obj : output.getUserInfoList())
                LOG.info(obj.getParticipantUserId() + " , " +
                        obj.getName() + " , " +
                        obj.getEmail().getValue() + " , " +
                        obj.getFirstName()
                        + " , " + obj.getLastName()
                        + " , " + obj.getJobTitle()
                );
        } else {
            LOG.info("No record");
        }
    }

}
