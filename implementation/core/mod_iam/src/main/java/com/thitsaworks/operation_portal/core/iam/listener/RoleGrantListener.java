package com.thitsaworks.operation_portal.core.iam.listener;

import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.model.RoleGrant;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class RoleGrantListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        RoleGrantListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onRoleGrantPersistOrUpdate(RoleGrant roleGrant) {

        var role = roleGrant.getRole();
        var actionData = new ActionData(roleGrant.getAction());

        iamEngine.getRoleGrantedActionsMap()
                 .computeIfAbsent(role.getRoleId(), k -> new HashSet<>())
                 .add(actionData);
    }

    @PostRemove
    public void onRoleGrantPostRemove(RoleGrant roleGrant) {

        var role = roleGrant.getRole();
        var actionData = new ActionData(roleGrant.getAction());

        iamEngine.getRoleGrantedActionsMap()
                 .computeIfAbsent(role.getRoleId(), k -> new HashSet<>())
                 .remove(actionData);
    }

}
