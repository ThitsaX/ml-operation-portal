package com.thitsaworks.operation_portal.core.participant.model.query;

import com.thitsaworks.operation_portal.component.infra.hazelcast.HazelcastConfiguration;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParticipantConfiguration.class, TestSettings.class, HazelcastConfiguration.class})
public class GetParticipantUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(GetParticipantUnitTest.class);

    @Autowired
    private ParticipantQuery participantQuery;

    @Test
    public void test_getParticipantSuccessfully() throws ParticipantNotFoundException {

        ParticipantData participantData =
                this.participantQuery.get(new ParticipantId(486547382195986432L));

        if (participantData != null) {
            LOG.info(participantData.dfspCode() + " , " +
                             participantData.name() + " , " +
                             participantData.address() + " , " +
                             participantData.mobile().getValue()
                             + " , " + participantData.businessContactId().getId().toString()
                             + " , " + participantData.technicalContactId().getId().toString()
            );

        } else {
            LOG.info("No record");
        }

    }

}
