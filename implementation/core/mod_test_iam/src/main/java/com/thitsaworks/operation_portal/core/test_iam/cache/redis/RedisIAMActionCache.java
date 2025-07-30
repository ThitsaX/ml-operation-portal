package com.thitsaworks.operation_portal.core.test_iam.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.IAMActionCache;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier(CacheQualifiers.REDIS)
@RequiredArgsConstructor
public class RedisIAMActionCache implements IAMActionCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisIAMActionCache.class);

    private static final String WITH_ID = "rd_action_with_id";

    private static final String WITH_ACTION_CODE = "rd_action_with_action_code";

    private final IAMActionRepository iamActionRepository;

    private final RedissonClient redissonClient;

    @Override
    public void save(ActionData actionData) {

        RMapCache<ActionId, ActionData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<ActionCode, ActionData> withActionCode = this.redissonClient.getMapCache(WITH_ACTION_CODE);

        var deleted = withId.remove(actionData.actionId());

        if (deleted != null) {
            withActionCode.remove(deleted.actionCode());
        }

        withId.put(actionData.actionId(), actionData);
        withActionCode.put(actionData.actionCode(), actionData);

    }

    @Override
    public Optional<ActionData> find(ActionId actionId) throws IAMException {

        RMapCache<ActionId, ActionData> withId = this.redissonClient.getMapCache(WITH_ID);

        var actionData = withId.get(actionId);

        var iamAction = this.iamActionRepository.findById(actionId)
                                                .orElseThrow(()->new IAMException(IAMErrors.ACTION_NOT_FOUND));

        if (actionData == null && iamAction != null) {

            actionData =new ActionData(iamAction.getActionId(),
                                       iamAction.getActionCode(),
                                       iamAction.getScope(),
                                       iamAction.getDescription());

            this.save(actionData);

        }else {
            return Optional.empty();
        }
        return Optional.of(actionData);
    }


    @Override
    public ActionData get(ActionCode actionCode) throws IAMException {
        RMapCache<ActionCode, ActionData> withActionCode = this.redissonClient.getMapCache(WITH_ACTION_CODE);

        ActionData actionData = withActionCode.get(actionCode);

        var iamAction = this.iamActionRepository.findOne(IAMActionRepository.Filters.withActionCode(actionCode))
                                                .orElseThrow(()->new IAMException(IAMErrors.ACTION_NOT_FOUND));

        if (actionData == null && iamAction != null) {

            actionData = new ActionData(iamAction.getActionId(),
                                        iamAction.getActionCode(),
                                        iamAction.getScope(),
                                        iamAction.getDescription());
            this.save(actionData);
        }else {
            return null;
        }
        return actionData;
    }


    @Override
    public List<ActionData> getAll() {

        RMapCache<ActionId, ActionData> withId = this.redissonClient.getMapCache(WITH_ID);

        List<ActionData> iamActions = new ArrayList<>();

        withId.values()
              .forEach(actionData -> {
                  iamActions.add(new ActionData(actionData.actionId(),
                                                actionData.actionCode(),
                                                actionData.scope(),
                                                actionData.description()));
              });

        return iamActions;
    }
    @Override
    public void delete(ActionId actionId) {

        RMapCache<ActionId, ActionData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<ActionCode, ActionData> withActionCode = this.redissonClient.getMapCache(WITH_ACTION_CODE);

        ActionData deleted = withId.remove(actionId);

        if (deleted != null) {
            withActionCode.remove(deleted.actionCode());
        }

    }
    @PostConstruct
    private void postConstruct() {

        for (var action : this.iamActionRepository.findAll()) {
            ActionData actionData = new ActionData(action.getActionId(),
                                                   action.getActionCode(),
                                                   action.getScope(),
                                                   action.getDescription());
            this.save(actionData);
        }
    }

}
