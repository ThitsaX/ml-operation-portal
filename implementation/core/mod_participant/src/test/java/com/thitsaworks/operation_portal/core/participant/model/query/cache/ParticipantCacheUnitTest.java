package com.thitsaworks.operation_portal.core.participant.model.query.cache;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.infra.redis.RedisConfiguration;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.ParticipantConfiguration;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.model.TestSettings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ParticipantConfiguration.class, TestSettings.class, RedisConfiguration.class})
public class ParticipantCacheUnitTest {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantCacheUnitTest.class);

    @Autowired
    @Qualifier(CacheQualifiers.DEFAULT)
    private ParticipantCache participantCache;

    @Test
    public void test_getParticipantsSuccessfully() {

        ParticipantData participantData = this.participantCache.get(new ParticipantId(413011549412544512L));

        LOG.info("Participant data: " + participantData);
        LOG.info("Participant data: " + participantData.participantId());

    }

}
