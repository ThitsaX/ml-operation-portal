package com.thitsaworks.operation_portal.core.test_iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.BlockedActionId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.test_iam.data.BlockedActionData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.BlockedAction;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;

import java.util.List;
import java.util.Optional;

public interface BlockedActionCache {

    void save(BlockedActionData blockedActionData);

    void delete(BlockedActionId blockedActionId);

    Optional<BlockedActionData> find(BlockedActionId blockedActionId) throws IAMException;

    BlockedActionData get(BlockedActionId blockedActionId) throws IAMException;

    List<BlockedActionData> getAll();

    class Updater{
        @PostPersist
        @PostUpdate
        public void persistOrUpdate(BlockedAction blockedAction){
            BlockedActionCache blockedActionCache= SpringContext.getBean(BlockedActionCache.class, CacheQualifiers.DEFAULT);

            BlockedActionData actionData =new BlockedActionData(blockedAction.getBlockedActionId(),
                                                               blockedAction.getUser()
                                                                            .getId(),
                                                               blockedAction.getIAMAction()
                                                                            .getActionId());
            blockedActionCache.save(actionData);
        }

        @PostUpdate
        public void postRemove(BlockedAction blockedAction){
            BlockedActionCache blockedActionCache= SpringContext.getBean(BlockedActionCache.class,CacheQualifiers.DEFAULT);

            blockedActionCache.delete(blockedAction.getBlockedActionId());
        }
    }


}
