package com.thitsaworks.operation_portal.core.iam.listener;

import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalRole;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class PrincipalRoleListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        PrincipalRoleListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onUserRolePersistOrUpdate(PrincipalRole principalRole) {

        var roleData = new RoleData(principalRole.getRole());
        var
            userId =
            principalRole.getPrincipal()
                         .getPrincipalId();

        iamEngine.getPrincipalRolesMap()
                 .computeIfAbsent(userId, k -> new HashSet<>())
                 .add(roleData);
    }

    @PostRemove
    public void onUserRolePostRemove(PrincipalRole principalRole) {

        var roleData = new RoleData(principalRole.getRole());
        var
            principalId =
            principalRole.getPrincipal()
                         .getPrincipalId();

        iamEngine.getPrincipalRolesMap()
                 .computeIfAbsent(principalId, k -> new HashSet<>())
                 .remove(roleData);
    }

}
