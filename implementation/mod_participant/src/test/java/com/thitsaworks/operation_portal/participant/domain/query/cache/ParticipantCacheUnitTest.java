package com.thitsaworks.operation_portal.participant.domain.query.cache;

import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import com.thitsaworks.operation_portal.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.participant.domain.persistence.MySqlDbSettings;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.query.cache.ParticipantCache;
import com.thitsaworks.operation_portal.participant.query.data.ParticipantData;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {ParticipantConfiguration.class, MySqlDbSettings.class})
public class ParticipantCacheUnitTest extends EnvAwareUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantCacheUnitTest.class);

    @Autowired
    @Qualifier(ParticipantCache.Strategies.DEFAULT)
    private ParticipantCache participantCache;

    @Test
    public void test_getParticipantsSuccessfully() {

        ParticipantData participantData = this.participantCache.get(new ParticipantId(413011549412544512L));

        LOG.info("Participant data: " + participantData);
        LOG.info("Participant data: " + participantData.getParticipantId());


//        for (ParticipantUserId id : participantData.getParticipantUserIds()) {
//
//            LOG.info("IDs: " + id);
//        }

    }

}
