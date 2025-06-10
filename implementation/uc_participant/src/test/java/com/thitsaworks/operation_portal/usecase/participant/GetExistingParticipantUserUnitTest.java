package com.thitsaworks.operation_portal.usecase.participant;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.usecase.ParticipantUseCaseConfiguration;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantUseCaseConfiguration.class, MySqlDbSettings.class})
public class GetExistingParticipantUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetExistingParticipantUserUnitTest.class);

    @Autowired
    private GetExistingParticipantUser getExistingParticipantUser;

    @Test
    public void test_getExistingParticipantUserSuccessfully() throws Exception {

        //   var output = this.getAllParticipantUser.execute(
        //    new GetAllParticipantUser.Input(new ParticipantId(390908599471210496L)));

        GetExistingParticipantUser.Output output = this.getExistingParticipantUser.execute(
                new GetExistingParticipantUser.Input(new ParticipantUserId(392628367895068672L)));

        if (output != null) {

            LOG.info(output.getParticipantUserId() + " , " +
                    output.getName() + " , " +
                    output.getEmail().getValue() + " , " +
                    output.getFirstName()
                    + " , " + output.getLastName()
                    + " , " + output.getJobTitle()
            );
        } else {
            LOG.info("No record");
        }
    }

}
