package com.thitsaworks.operation_portal.core.test_iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.BlockedActionId;
import com.thitsaworks.operation_portal.core.test_iam.model.BlockedAction;

import java.io.Serializable;

public record BlockedActionData(BlockedActionId blockedActionId,
                                UserId userId,
                                ActionId actionId) implements Serializable {
    public BlockedActionData(BlockedAction blockedAction){
        this(blockedAction.getId(),
             blockedAction.getUser()
                          .getId(),
             blockedAction.getIAMAction()
                          .getActionId());
    }
}
