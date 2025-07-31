package com.thitsaworks.operation_portal.core.test_iam.listener;

import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IAMActionListener {

    private static IAMEngine iamEngine;

    @Autowired
    private IAMEngine iamEngineInstance;

    @PostConstruct
    public void init() {

        IAMActionListener.iamEngine = iamEngineInstance;
    }

    @PostPersist
    @PostUpdate
    public void onIAMActionPersistOrUpdate(IAMAction iamAction) {

        iamEngine.getActionCodesMap().put(iamAction.getActionCode(), new ActionData(iamAction));
        iamEngine.getActionIdsMap().put(iamAction.getActionId(), new ActionData(iamAction));
    }

    @PostRemove
    public void onIAMActionPostRemove(IAMAction iamAction) {

       iamEngine.getActionCodesMap().remove(iamAction.getActionCode());
       iamEngine.getActionIdsMap().remove(iamAction.getActionId());
    }

}
