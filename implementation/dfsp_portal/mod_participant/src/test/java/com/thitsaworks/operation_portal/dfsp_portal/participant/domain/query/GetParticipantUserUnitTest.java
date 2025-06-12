package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.query;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.dfsp_portal.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.GetUser;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

public class GetParticipantUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantUserUnitTest.class);

    @Autowired
    private GetUser getUser;

    @Test
    public void test_getUsersSuccessfully() throws ParticipantUserNotFoundException {

        GetUser.Output output = this.getUser.execute(new GetUser.Input(new ParticipantUserId(39262836789506867L)));

        if (output != null) {

            LOG.info(output.getName() + " , " + output.getParticipantUserId().getId().toString());
        } else {
            LOG.info("No record");
        }
    }

}
