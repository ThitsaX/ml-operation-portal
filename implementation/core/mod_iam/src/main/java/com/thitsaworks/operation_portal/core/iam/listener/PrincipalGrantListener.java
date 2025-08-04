package com.thitsaworks.operation_portal.core.iam.listener;

import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalGrant;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class PrincipalGrantListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        PrincipalGrantListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onUserGrantPersistOrUpdate(PrincipalGrant principalGrant) {

        var
            principalId =
            principalGrant.getPrincipal()
                          .getPrincipalId();
        var actionData = new ActionData(principalGrant.getAction());

        iamEngine.addPrincipalGrantedAction(principalId, actionData);
    }

    @PostRemove
    public void onUserGrantPostRemove(PrincipalGrant principalGrant) {

        var
            principalId =
            principalGrant.getPrincipal()
                          .getPrincipalId();
        var actionData = new ActionData(principalGrant.getAction());

        iamEngine.removePrincipalGrantedAction(principalId, actionData);
    }

}
