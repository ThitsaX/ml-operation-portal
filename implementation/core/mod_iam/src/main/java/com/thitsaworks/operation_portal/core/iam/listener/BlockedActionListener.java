package com.thitsaworks.operation_portal.core.iam.listener;

import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.model.BlockedAction;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class BlockedActionListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        BlockedActionListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onBlockedActionPersistOrUpdate(BlockedAction blockedAction) {

        var
            principalId =
            blockedAction.getPrincipal()
                         .getPrincipalId();
        var actionData = new ActionData(blockedAction.getAction());

        iamEngine.getPrincipalDeniedActionsMap()
                 .computeIfAbsent(principalId, k -> new HashSet<>())
                 .add(actionData);
    }

    @PostRemove
    public void onBlockedActionPostRemove(BlockedAction blockedAction) {

        var
            principalId =
            blockedAction.getPrincipal()
                         .getPrincipalId();
        var actionData = new ActionData(blockedAction.getAction());

        iamEngine.getPrincipalDeniedActionsMap()
                 .computeIfAbsent(principalId, k -> new HashSet<>())
                 .remove(actionData);
    }

}
