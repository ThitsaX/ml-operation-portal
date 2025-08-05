package com.thitsaworks.operation_portal.core.iam.listener;

import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.model.Action;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActionListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        ActionListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onIAMActionPersistOrUpdate(Action action) {

        if (ActionListener.iamEngine != null) {

            iamEngine.addAction(action.getActionId(), action.getActionCode(), new ActionData(action));
        }

    }

    @PostRemove
    public void onIAMActionPostRemove(Action action) {

        if (ActionListener.iamEngine != null) {

            iamEngine.removeAction(action.getActionId(), action.getActionCode(), new ActionData(action));
        }

    }

}
