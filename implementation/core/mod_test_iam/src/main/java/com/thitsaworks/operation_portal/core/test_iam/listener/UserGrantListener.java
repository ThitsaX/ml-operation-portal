package com.thitsaworks.operation_portal.core.test_iam.listener;

import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.test_iam.model.UserGrant;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserGrantListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        UserGrantListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onUserGrantPersistOrUpdate(UserGrant userGrant) {

        var
            userId =
            userGrant.getUser()
                     .getUserId();
        var actionData = new ActionData(userGrant.getIAMAction());

        iamEngine.getUserGrantedActionsMap()
                 .computeIfAbsent(userId, k -> new HashSet<>())
                 .add(actionData);
    }

    @PostRemove
    public void onUserGrantPostRemove(UserGrant userGrant) {

        var
            userId =
            userGrant.getUser()
                     .getUserId();
        var actionData = new ActionData(userGrant.getIAMAction());

        iamEngine.getUserGrantedActionsMap()
                 .computeIfAbsent(userId, k -> new HashSet<>())
                 .remove(actionData);
    }

}
