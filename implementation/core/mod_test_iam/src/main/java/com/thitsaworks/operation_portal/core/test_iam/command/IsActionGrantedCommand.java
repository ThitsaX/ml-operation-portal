package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface IsActionGrantedCommand {
    Output execute(Input input) throws IAMException;

    record Input(UserId userId,
                 ActionId actionId) { }

    record Output(boolean granted) { }

}
