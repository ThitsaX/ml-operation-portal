package com.thitsaworks.operation_portal.core.participant.model.query;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParticipantConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class GetParticipantUserUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantUserUnitTest.class);

    @Autowired
    private ParticipantUserQuery participantUserQuery;

    @Test
    public void test_getParticipantUsersSuccessfully() throws ParticipantNotFoundException,
                                                              ParticipantUserNotFoundException {

        List<ParticipantUserData> participantUserDataList =
            this.participantUserQuery.getParticipantUsers(new ParticipantId(486547382195986432L));

        if (participantUserDataList != null) {

            LOG.info("Participant Users  : [{}]", participantUserDataList);
        } else {
            LOG.info("No record");
        }

    }

    @Test
    public void test_getParticipantUserSuccessfully() throws ParticipantNotFoundException,
                                                             ParticipantUserNotFoundException {

        ParticipantUserData participantUserData =
            this.participantUserQuery.get(new ParticipantUserId(486549751625580544L));

        if (participantUserData != null) {

            LOG.info("Participant User  : [{}]", participantUserData);
        } else {
            LOG.info("No record");
        }

    }

}
