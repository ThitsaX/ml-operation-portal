package com.thitsaworks.operation_portal.core.test_iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import java.util.List;
import java.util.Optional;

public interface IAMActionCache {

    void save(ActionData actionData);

    Optional<ActionData> find(ActionId actionId) throws IAMException;

    ActionData get(ActionCode actionCode) throws IAMException;

    List<ActionData> getAll();

    void delete(ActionId actionId);

    class Updater {

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(IAMAction iamAction) {

            IAMActionCache iamActionCache = SpringContext.getBean(IAMActionCache.class, CacheQualifiers.DEFAULT);

            ActionData actionData = new ActionData(iamAction.getActionId(),
                                                   iamAction.getActionCode(),
                                                   iamAction.getScope(),
                                                   iamAction.getDescription());

            iamActionCache.save(actionData);

        }
        @PostRemove
        public void postRemove(IAMAction iamAction){
            IAMActionCache iamActionCache= SpringContext.getBean(IAMActionCache.class , CacheQualifiers.DEFAULT);

            iamActionCache.delete(iamAction.getActionId());
        }

    }

}