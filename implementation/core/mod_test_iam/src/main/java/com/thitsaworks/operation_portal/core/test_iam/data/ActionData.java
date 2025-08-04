package com.thitsaworks.operation_portal.core.test_iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;

import java.io.Serializable;

public record ActionData(ActionId actionId,
                         ActionCode actionCode,
                         String scope,
                         String description) implements Serializable {

    public ActionData(IAMAction iamAction) {

        this(iamAction.getActionId(),
             iamAction.getActionCode(),
             iamAction.getScope(),
             iamAction.getDescription());
    }

}
