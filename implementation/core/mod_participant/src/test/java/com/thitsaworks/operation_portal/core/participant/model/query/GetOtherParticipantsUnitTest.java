package com.thitsaworks.operation_portal.core.participant.model.query;

import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.type.ParticipantInfo;
import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import com.thitsaworks.operation_portal.core.participant.query.GetOtherParticipantsQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParticipantConfiguration.class, TestSettings.class, HazelcastConfiguration.class})
public class GetOtherParticipantsUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetOtherParticipantsUnitTest.class);

    @Autowired
    private GetOtherParticipantsQuery getOtherParticipantsQuery;

    @Test
    public void test_getParticipantSuccessfully() throws ParticipantNotFoundException {

        List<ParticipantInfo> participantData =
                this.getOtherParticipantsQuery.getOtherParticipants(new ParticipantId(486547382195986432L));

        if (participantData != null) {LOG.info("Participant Users  : [{}]", participantData);

        } else {
            LOG.info("No record");
        }

    }

}
