package com.thitsaworks.operation_portal.core.participant.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.ParticipantUserCache;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
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
public class RedisParticipantUserCache implements ParticipantUserCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisParticipantUserCache.class);

    private static final String WITH_ID = "rd_participant_user_with_id";

    private final ParticipantUserRepository participantUserRepository;

    private final RedissonClient redissonClient;

    @Override
    public void save(ParticipantUserData participantUserData) {

        RMapCache<Long, ParticipantUserData> withId = this.redissonClient.getMapCache(WITH_ID);

        withId.remove(participantUserData.participantUserId()
                                         .getEntityId());

        withId.put(participantUserData.participantUserId()
                                      .getEntityId(), participantUserData);

    }

    @Override
    public ParticipantUserData get(ParticipantUserId participantUserId) {

        RMapCache<Long, ParticipantUserData> withId = this.redissonClient.getMapCache(WITH_ID);

        return withId.get(participantUserId.getId());
    }

    @Override
    public void delete(ParticipantUserId participantUserId) {

        RMapCache<Long, ParticipantUserData> withId = this.redissonClient.getMapCache(WITH_ID);

        withId.remove(participantUserId.getId());

    }

}
