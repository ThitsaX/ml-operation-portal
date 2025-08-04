package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface ToggleUserStatusCommand {
    Output execute(Input input) throws IAMException;

    record Input(UserId userId) { }

    record Output(String resultCode, boolean active) { }

}

