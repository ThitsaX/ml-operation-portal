package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface RemoveRoleFromUserCommand {
    Output execute(Input input) throws IAMException;

    record Input(UserId userId, RoleId roleId) { }

    record Output(boolean removed) { }

}

