package com.thitsaworks.operation_portal.core.iam.listener;

import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.model.Role;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        roleGrants.forEach(action -> {
            iamEngine.addRoleGrantedAction(role.getRoleId(), action);
        });

    }

    @PostRemove
    public void onRolePostRemove(Role role) {

        var roleData = new RoleData(role);

        var
            principalIdList =
            iamEngine.getPrincipalRolesMap()
                     .entrySet()
                     .stream()
                     .filter(entry -> entry.getValue()
                                           .contains(roleData))
                     .map(Map.Entry::getKey)
                     .collect(Collectors.toSet());


        iamEngine.removeRoleGrantedAction(role.getRoleId(), null);

        for (var principalId : principalIdList) {
            iamEngine.removePrincipalRole(principalId, roleData);
        }
    }

}
