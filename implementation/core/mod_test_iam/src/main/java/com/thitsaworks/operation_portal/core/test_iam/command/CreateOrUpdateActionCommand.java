package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;

public interface CreateOrUpdateActionCommand {
    Output execute(Input input);

    record Input(
        ActionCode actionCode,
        String scope,
        String description) { }

    record Output(ActionId actionId) { }

}

