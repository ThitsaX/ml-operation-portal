package com.thitsaworks.operation_portal.participant.domain.query;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.query.GetUsers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class GetUsersUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetUsersUnitTest.class);

    @Autowired
    private GetUsers getUsers;

    @Test
    public void test_getUsersSuccessfully() {

        GetUsers.Output output = this.getUsers.execute(new GetUsers.Input(new ParticipantId(390908599471210497L)));

        if (output.getUserInfoList() != null) {
            for (var obj : output.getUserInfoList())
                LOG.info(obj.getName() + " , " + obj.getStatus());
        } else {
            LOG.info("No record");
        }

    }

}
