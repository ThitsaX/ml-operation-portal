package com.thitsaworks.operation_portal.core.test_iam.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.UserGrantCache;
import com.thitsaworks.operation_portal.core.test_iam.data.UserGrantData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserGrantRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier(CacheQualifiers.REDIS)
@RequiredArgsConstructor
public class RedisUserGrantCache implements UserGrantCache {

    private static final String WITH_ID = "rd_grant_id";
    private static final String USER_ID = "rd_user_id";
    private static final String ACTION_ID = "rd_action_id";

    private final UserGrantRepository userGrantRepository;

    private final RedissonClient redissonClient;

    @Override
    public void save(UserGrantData userGrantData) {
        RMapCache<GrantId, UserGrantData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<UserId, UserGrantData> withUserId = this.redissonClient.getMapCache(USER_ID);
        RMapCache<ActionId, UserGrantData> withActionId = this.redissonClient.getMapCache(ACTION_ID);

        var deleted = withId.remove(userGrantData.grantId());

        if (deleted != null) {
            withUserId.remove(deleted.userId());
            withActionId.remove(deleted.actionId());
        }

        withId.put(userGrantData.grantId(), userGrantData);
        withUserId.put(userGrantData.userId(), userGrantData);
        withActionId.put(userGrantData.actionId(), userGrantData);

    }

    @Override
    public void delete(GrantId grantId) {
        RMapCache<GrantId, UserGrantData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<UserId, UserGrantData> withUserId = this.redissonClient.getMapCache(USER_ID);
        RMapCache<ActionId, UserGrantData> withActionId = this.redissonClient.getMapCache(ACTION_ID);

        var deleted = withId.remove(grantId);

        if (deleted != null) {
            withUserId.remove(deleted.userId());
            withActionId.remove(deleted.actionId());
        }

    }




    @Override
    public Optional<UserGrantData> find(GrantId grantId) throws IAMException {

        RMapCache<GrantId, UserGrantData> withId = this.redissonClient.getMapCache(WITH_ID);

        var userGrantData = withId.get(grantId);

        var userGrant = this.userGrantRepository.findById(grantId)
                                                .orElseThrow(() -> new IAMException(IAMErrors.USER_NOT_FOUND));

        if (userGrantData == null && userGrant != null) {
            userGrantData = new UserGrantData(userGrant.getGrantId(),
                                              userGrant.getUser()
                                                       .getUserId(),
                                              userGrant.getIAMAction()
                                                       .getActionId());
            this.save(userGrantData);
        }
        else {
            return Optional.empty();
        }

        return Optional.of(userGrantData);
    }

    @Override
    public UserGrantData get(GrantId grantId) throws IAMException {
        RMapCache<GrantId, UserGrantData> withId = this.redissonClient.getMapCache(WITH_ID);
        var userGrantData = withId.get(grantId);
        var userGrant = this.userGrantRepository.findById(grantId)
                                                .orElseThrow(() -> new IAMException(IAMErrors.USER_NOT_FOUND));
        if (userGrantData == null && userGrant != null) {
            userGrantData = new UserGrantData(userGrant.getGrantId(),
                                              userGrant.getUser()
                                                       .getUserId(),
                                              userGrant.getIAMAction()
                                                       .getActionId());
            this.save(userGrantData);
        }
        return userGrantData;
    }
    @Override
    public List<UserGrantData> getAll() {
        RMapCache<GrantId, UserGrantData> withId = this.redissonClient.getMapCache(WITH_ID);
        List<UserGrantData> userGrantDataList = new ArrayList<>();

        for( GrantId grantId : withId.keySet()) {
            var userGrantData = withId.get(grantId);
            if (userGrantData != null) {
                userGrantDataList.add(userGrantData);
            }
        }
        return userGrantDataList;
    }
    @PostConstruct
    private void postConstruct() {
        for(var userGrant : this.userGrantRepository.findAll()) {
            UserGrantData userGrantData = new UserGrantData(userGrant.getGrantId(),
                                                            userGrant.getUser()
                                                                     .getUserId(),
                                                            userGrant.getIAMAction()
                                                                     .getActionId());
            this.save(userGrantData);
        }
    }
}
