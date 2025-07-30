package com.thitsaworks.operation_portal.core.test_iam.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.IAMCache;

import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier(CacheQualifiers.REDIS)
@RequiredArgsConstructor
public class RedisIAMCache implements IAMCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisIAMCache.class);

    private static final String WITH_ID = "rd_user_with_id";

    private static final String WITH_ACCESS_KEY = "rd_user_with_access_key";

    private static final String WITH_REALM_TYPE ="rd_user_with_realm_type";

    private final UserRepository userRepository;

    private final RedissonClient redissonClient;

    @Override
    public void save(UserData userData) {
        RMapCache<Long, UserData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<Long, UserData> withAccessKey = this.redissonClient.getMapCache(WITH_ACCESS_KEY);

        var deleted = withId.remove(userData.userId()
                                            .getEntityId());

        if (deleted != null) {

            withAccessKey.remove(deleted.accessKey().getEntityId());
        }


        withId.put(userData.userId()
                           .getEntityId(), userData);

        withAccessKey.put(userData.accessKey()
                                  .getId(), userData);



    }

    @Override
    public void delete(UserId id) {
        RMapCache<Long, UserData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<AccessKey, UserData> withAccessKey = this.redissonClient.getMapCache(WITH_ACCESS_KEY);

        UserData deleted = null;

        deleted = withId.remove(id.getEntityId());

        if (deleted != null) {

            withAccessKey.remove(deleted.accessKey());
        }

    }


    @Override
    public Optional<UserData> find(AccessKey accessKey) {

        RMapCache<AccessKey, UserData> withAccessKey = this.redissonClient.getMapCache(WITH_ACCESS_KEY);

        var data = withAccessKey.get(accessKey);

        if (data == null) {

            var entity = this.userRepository.findOne(UserRepository.Filters.withAccessKey(accessKey))
                                            .orElse(null);

            if (entity == null) {

                return Optional.empty();
            }

            data = new UserData(entity.getUserId(),
                                entity.getAccessKey(),
                                entity.getSecretKey(),
                                entity.getRealmType(),
                                entity.getRealmId(),
                                entity.getPrincipalStatus());

            this.save(data);
        }
        return Optional.of(data);
    }

    @Override
    public UserData get(UserId id) throws IAMException {

        RMapCache<Long, UserData> withId = this.redissonClient.getMapCache(WITH_ID);

        var data = withId.get(id.getEntityId());

        var entity = this.userRepository.findById(id)
                                        .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        if (data == null && entity != null) {

            data = new UserData(entity.getUserId(),
                                entity.getAccessKey(),
                                entity.getSecretKey(),
                                entity.getRealmType(),
                                entity.getRealmId(),
                                entity.getPrincipalStatus());

            this.save(data);
        }
        return data;
    }

    @Override
    public UserData get(AccessKey accessKey, RealmType realmType) throws IAMException {
        RMapCache<Long, UserData> withAccessKey = this.redissonClient.getMapCache(WITH_ACCESS_KEY);
        RMapCache<Long, UserData> withRealmType = this.redissonClient.getMapCache(WITH_REALM_TYPE);

        var data = withAccessKey.get(accessKey.getId());

        var entity = this.userRepository.findOne(UserRepository.Filters.withAccessKey(accessKey))
                                        .orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        if (data == null && entity != null) {

            data = new UserData(entity.getUserId(),
                                entity.getAccessKey(),
                                entity.getSecretKey(),
                                entity.getRealmType(),
                                entity.getRealmId(),
                                entity.getPrincipalStatus());

            this.save(data);
        }

        return data;

    }


    @Override
    public List<UserData> getAll() {
        List<UserData> dataList = new ArrayList<>();

        RMapCache<Long, UserData> withId = this.redissonClient.getMapCache(WITH_ID);

        for (Long key : withId.keySet()) {

            UserData data = withId.get(key);
            dataList.add(data);

        }

        return dataList;

    }

    @Override
    public void delete(AccessKey accessKey) {
        RMapCache<Long, UserData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<Long, UserData> withAccessKey = this.redissonClient.getMapCache(WITH_ACCESS_KEY);

        UserData deleted = null;

        deleted = withAccessKey.remove(accessKey.getId());

        if (deleted != null) {

            withId.remove(deleted.userId()
                                 .getId());
        }

    }
    @PostConstruct
    private void postConstruct() {

        for (var user : this.userRepository.findAll().stream()
                                            .map(entity -> new UserData(entity.getUserId(),
                                                                        entity.getAccessKey(),
                                                                        entity.getSecretKey(),
                                                                        entity.getRealmType(),
                                                                        entity.getRealmId(),
                                                                        entity.getPrincipalStatus()))
                                            .toList()) {

            this.save(user);
        }


    }}