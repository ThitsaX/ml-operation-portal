package com.thitsaworks.operation_portal.core.test_iam.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.BlockedActionId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.BlockedActionCache;
import com.thitsaworks.operation_portal.core.test_iam.data.BlockedActionData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.BlockedActionRepository;
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
public class RedisBlockedActionCache implements BlockedActionCache {

    private static final String WITH_ID = "rd_blocked_action_with_id";
    private static final String WITH_USER_ID = "rd_blocked_action_with_user_id";
    private static final String WITH_ACTION_ID = "rd_blocked_action_with_action_id";

    private final BlockedActionRepository blockedActionRepository;

    private final RedissonClient redissonClient;

    @Override
    public void save(BlockedActionData blockedActionData) {

        RMapCache<BlockedActionId, BlockedActionData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<UserId, BlockedActionData> withUserId = this.redissonClient.getMapCache(WITH_USER_ID);
        RMapCache<ActionId, BlockedActionData> withActionId = this.redissonClient.getMapCache(WITH_ACTION_ID);

        var deleted = withId.remove(blockedActionData.blockedActionId());

        if (deleted != null) {
            withUserId.remove(deleted.userId());
            withActionId.remove(deleted.actionId());
        }

        withId.put(blockedActionData.blockedActionId(), blockedActionData);
        withUserId.put(blockedActionData.userId(), blockedActionData);
        withActionId.put(blockedActionData.actionId(), blockedActionData);
    }
    @Override
    public void delete(BlockedActionId blockedActionId) {

        RMapCache<BlockedActionId, BlockedActionData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<UserId, BlockedActionData> withUserId = this.redissonClient.getMapCache(WITH_USER_ID);
        RMapCache<ActionId, BlockedActionData> withActionId = this.redissonClient.getMapCache(WITH_ACTION_ID);

        var deleted = withId.remove(blockedActionId);

        if (deleted != null) {
            withUserId.remove(deleted.userId());
            withActionId.remove(deleted.actionId());
        }

    }

    @Override
    public Optional<BlockedActionData> find(BlockedActionId blockedActionId) throws IAMException {
        RMapCache<BlockedActionId, BlockedActionData> withId = this.redissonClient.getMapCache(WITH_ID);

        var blockedActionData = withId.get(blockedActionId);

        var blockedAction = this.blockedActionRepository.findById(blockedActionId)
                                                        .orElseThrow(() -> new IAMException(IAMErrors.BLOCKED_ACTION_NOT_FOUND));

        if (blockedActionData == null && blockedAction != null) {
            blockedActionData = new BlockedActionData(blockedAction.getBlockedActionId(),
                                                      blockedAction.getUser().getId(),
                                                      blockedAction.getIAMAction().getActionId());

            this.save(blockedActionData);
        } else {
            return Optional.empty();
        }

        return Optional.of(blockedActionData);
    }


    @Override
    public BlockedActionData get(BlockedActionId blockedActionId) throws IAMException {

        RMapCache<BlockedActionId, BlockedActionData> withId = this.redissonClient.getMapCache(WITH_ID);

        BlockedActionData blockedActionData = withId.get(blockedActionId);

        var blockedAction = this.blockedActionRepository.findById(blockedActionId)
                                                        .orElseThrow(() -> new IAMException(IAMErrors.BLOCKED_ACTION_NOT_FOUND));

        if (blockedActionData == null && blockedAction != null) {
            blockedActionData = new BlockedActionData(blockedAction.getBlockedActionId(),
                                                      blockedAction.getUser()
                                                                   .getId(),
                                                      blockedAction.getIAMAction()
                                                                   .getActionId());
            this.save(blockedActionData);
        }

        return blockedActionData;
    }

    @Override
    public List<BlockedActionData> getAll() {

        RMapCache<BlockedActionId, BlockedActionData> withId = this.redissonClient.getMapCache(WITH_ID);

        List<BlockedActionData> blockedActionDataList = new ArrayList<>();

        for (BlockedActionData blockedActionData : withId.values()) {
            if (blockedActionData != null) {
                blockedActionDataList.add(blockedActionData);
            }
        }
        return blockedActionDataList;
    }

    @PostConstruct
    private void postConstruct(){

        for(var blockedAction : this.blockedActionRepository.findAll()) {

            BlockedActionData actionData = new BlockedActionData(blockedAction.getBlockedActionId(),
                                                                 blockedAction.getUser()
                                                                                  .getId(),
                                                                 blockedAction.getIAMAction()
                                                                              .getActionId());
            this.save(new BlockedActionData(blockedAction));
        }
    }

}
