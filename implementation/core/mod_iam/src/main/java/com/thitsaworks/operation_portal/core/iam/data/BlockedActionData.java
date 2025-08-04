package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.BlockedActionId;
import com.thitsaworks.operation_portal.core.iam.model.BlockedAction;

import java.io.Serializable;

public record BlockedActionData(BlockedActionId blockedActionId,
                                PrincipalId principalId,
                                ActionId actionId) implements Serializable {

    public BlockedActionData(BlockedAction blockedAction) {

        this(blockedAction.getId(),
             blockedAction.getPrincipal()
                          .getPrincipalId(),
             blockedAction.getAction()
                          .getActionId());
    }

}
