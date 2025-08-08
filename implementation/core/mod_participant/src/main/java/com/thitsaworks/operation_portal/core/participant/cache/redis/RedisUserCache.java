package com.thitsaworks.operation_portal.core.participant.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.UserCache;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
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
public class RedisUserCache implements UserCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUserCache.class);

    private static final String WITH_ID = "rd_user_with_id";

    private final RedissonClient redissonClient;

    @Override
    public void save(UserData userData) {

        RMapCache<Long, UserData> withId = this.redissonClient.getMapCache(WITH_ID);

        withId.remove(userData.userId()
                              .getEntityId());

        withId.put(userData.userId()
                           .getEntityId(), userData);

    }

    @Override
    public UserData get(UserId userId) {

        RMapCache<Long, UserData> withId = this.redissonClient.getMapCache(WITH_ID);

        return withId.get(userId.getId());
    }

    @Override
    public void delete(UserId userId) {

        RMapCache<Long, UserData> withId = this.redissonClient.getMapCache(WITH_ID);

        withId.remove(userId.getId());

    }

}
