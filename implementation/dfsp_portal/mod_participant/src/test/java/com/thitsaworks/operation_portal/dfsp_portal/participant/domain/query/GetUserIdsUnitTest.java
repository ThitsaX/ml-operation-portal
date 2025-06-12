package com.thitsaworks.operation_portal.dfsp_portal.participant.domain.query;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.GetUserIds;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

public class GetUserIdsUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetUserIdsUnitTest.class);

    @Autowired
    private GetUserIds getUserIds;

    @Test
    public void test_getUserIdsSuccessfully() {

        GetUserIds.Output output =
                this.getUserIds.execute(new GetUserIds.Input(new ParticipantId(392623203375808512L)));
        LOG.info("Size: " + output.getParticipantUserIds().size());

        for (ParticipantUserId id : output.getParticipantUserIds()) {

            LOG.info("id: " + id);
        }

    }

}
