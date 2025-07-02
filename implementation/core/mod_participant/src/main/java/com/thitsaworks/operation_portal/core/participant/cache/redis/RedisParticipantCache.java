package com.thitsaworks.operation_portal.core.participant.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier(CacheQualifiers.REDIS)
@RequiredArgsConstructor
public class RedisParticipantCache implements ParticipantCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisParticipantCache.class);

    private static final String WITH_ID = "rd_participant_with_id";

    private final ParticipantRepository participantRepository;

    private final RedissonClient redissonClient;

    @Override
    public void save(ParticipantData participantData) {

        RMapCache<Long, ParticipantData> withId = this.redissonClient.getMapCache(WITH_ID);

        withId.remove(participantData.participantId()
                                     .getEntityId());

        withId.put(participantData.participantId()
                                  .getEntityId(), participantData);
    }

    @Override
    public ParticipantData get(ParticipantId participantId) {

        RMapCache<Long, ParticipantData> withId = this.redissonClient.getMapCache(WITH_ID);

        return withId.get(participantId.getId());
    }

    @Override
    public void delete(ParticipantId participantId) {

        RMapCache<Long, ParticipantData> withId = this.redissonClient.getMapCache(WITH_ID);

        withId.remove(participantId.getId());
    }

}
