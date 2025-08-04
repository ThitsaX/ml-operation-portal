package com.thitsaworks.operation_portal.core.test_iam.listener;

import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.test_iam.model.UserRole;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class UserRoleListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        UserRoleListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onUserRolePersistOrUpdate(UserRole userRole) {

        var roleData = new RoleData(userRole.getRole());
        var
            userId =
            userRole.getUser()
                    .getUserId();

        iamEngine.getUserRolesMap()
                 .computeIfAbsent(userId, k -> new HashSet<>())
                 .add(roleData);
    }

    @PostRemove
    public void onUserRolePostRemove(UserRole userRole) {

        var roleData = new RoleData(userRole.getRole());
        var
            userId =
            userRole.getUser()
                    .getUserId();

        iamEngine.getUserRolesMap()
                 .computeIfAbsent(userId, k -> new HashSet<>())
                 .remove(roleData);
    }

}
