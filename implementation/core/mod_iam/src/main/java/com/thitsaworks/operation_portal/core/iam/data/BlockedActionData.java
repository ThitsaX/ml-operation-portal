package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.BlockedActionId;
import com.thitsaworks.operation_portal.core.iam.model.BlockedAction;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlockedActionData that = (BlockedActionData) o;
        return Objects.equals(blockedActionId, that.blockedActionId);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(blockedActionId);
    }


}
