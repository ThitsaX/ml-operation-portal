package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface CreateRoleCommand {
    Output execute(Input input) throws IAMException;

    record Input(RoleId roleId, String name){}

    record Output(RoleId roleId){}
}

