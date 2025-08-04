package com.thitsaworks.operation_portal.core.iam.listener;

import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalData;
import com.thitsaworks.operation_portal.core.iam.data.RoleData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PrincipalListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        PrincipalListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onUserPersistOrUpdate(Principal principal) {

        Set<RoleData>
            principalRoles = principal.getRoles()
                                      .stream()
                                      .map(RoleData::new)
                                      .collect(Collectors.toSet());

        Set<ActionData>
            principalGrants = principal.getGrantedActions()
                                       .stream()
                                       .map(ActionData::new)
                                       .collect(Collectors.toSet());

        Set<ActionData>
            principalDenies = principal.getDeniedActions()
                                       .stream()
                                       .map(ActionData::new)
                                       .collect(Collectors.toSet());

        var principalData = new PrincipalData(principal);

        iamEngine.addPrincipal(principal.getPrincipalId(), principalData);
        principalRoles.forEach(role -> iamEngine.addPrincipalRole(principal.getPrincipalId(), role));
        principalGrants.forEach(action -> iamEngine.addPrincipalGrantedAction(principal.getPrincipalId(), action));
        principalDenies.forEach(action -> iamEngine.addPrincipalDeniedAction(principal.getPrincipalId(), action));
    }

    @PostRemove
    public void onUserPostRemove(Principal principal) {

        var principalId = principal.getPrincipalId();

        Set<RoleData>
            principalRoles = principal.getRoles()
                                      .stream()
                                      .map(RoleData::new)
                                      .collect(Collectors.toSet());

        Set<ActionData>
            principalGrants = principal.getGrantedActions()
                                       .stream()
                                       .map(ActionData::new)
                                       .collect(Collectors.toSet());

        Set<ActionData>
            principalDenies = principal.getDeniedActions()
                                       .stream()
                                       .map(ActionData::new)
                                       .collect(Collectors.toSet());

        var principalData = new PrincipalData(principal);

        iamEngine.removePrincipal(principalId, principalData);
        principalRoles.forEach(role -> iamEngine.removePrincipalRole(principalId, role));
        principalGrants.forEach(action -> iamEngine.removePrincipalGrantedAction(principalId, action));
        principalDenies.forEach(action -> iamEngine.removePrincipalDeniedAction(principalId, action));

    }

}
