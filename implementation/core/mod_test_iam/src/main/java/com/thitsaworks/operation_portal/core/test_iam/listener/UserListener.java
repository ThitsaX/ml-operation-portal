package com.thitsaworks.operation_portal.core.test_iam.listener;

import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        UserListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onUserPersistOrUpdate(User user) {

        Set<RoleData>
            userRoles =
            user.getRoles()
                .stream()
                .map(RoleData::new)
                .collect(Collectors.toSet());

        Set<ActionData>
            userGrants =
            user.getGrantedActions()
                .stream()
                .map(ActionData::new)
                .collect(Collectors.toSet());

        var userData = new UserData(user);
        iamEngine.getUsersMap()
                 .put(user.getUserId(), userData);

        // Update user roles
        iamEngine.getUserRolesMap()
                 .put(user.getUserId(), userRoles);

        // Update user granted actions
        iamEngine.getUserGrantedActionsMap()
                 .put(user.getUserId(), userGrants);
    }

    @PostRemove
    public void onUserPostRemove(User user) {

        var userId = user.getUserId();

        iamEngine.getUsersMap()
                 .remove(userId);
        iamEngine.getUserRolesMap()
                 .remove(userId);
        iamEngine.getUserGrantedActionsMap()
                 .remove(userId);
        iamEngine.getUserDeniedActionsMap()
                 .remove(userId);
    }

}
