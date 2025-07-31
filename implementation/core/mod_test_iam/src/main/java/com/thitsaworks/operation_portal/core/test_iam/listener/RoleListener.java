package com.thitsaworks.operation_portal.core.test_iam.listener;

import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        RoleListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onRolePersistOrUpdate(Role role) {

        Set<ActionData>
            roleGrants =
            role.getGrantedActions()
                .stream()
                .map(ActionData::new)
                .collect(Collectors.toSet());

        iamEngine.getRoleGrantedActionsMap()
                 .computeIfAbsent(role.getRoleId(), k -> new HashSet<>())
                 .addAll(roleGrants);
    }

    @PostRemove
    public void onRolePostRemove(Role role) {

        var roleData = new RoleData(role);

        var
            userIdList =
            iamEngine.getUserRolesMap()
                     .entrySet()
                     .stream()
                     .filter(entry -> entry.getValue()
                                           .contains(roleData))
                     .map(Map.Entry::getKey)
                     .collect(Collectors.toSet());

        iamEngine.getRoleGrantedActionsMap()
                 .remove(role.getRoleId());

        for (var userId : userIdList) {
            iamEngine.getUserRolesMap()
                     .get(userId)
                     .remove(roleData);
        }
    }

}
