package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.model.Action;

import java.io.Serializable;
import java.util.Objects;

public record ActionData(ActionId actionId,
                         ActionCode actionCode,
                         String scope,
                         String description) implements Serializable {

    public ActionData(Action action) {

        this(action.getActionId(),
             action.getActionCode(),
             action.getScope(),
             action.getDescription());
    }


    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActionData that = (ActionData) o;
        return Objects.equals(actionId, that.actionId);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(actionId);
    }


}
