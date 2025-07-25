package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface GrantRoleActionCommand {
    Output execute(Input input) throws IAMException;

    record Input(
        RoleId roleId,
        ActionId actionId) { }

    record Output(boolean resultCode) { }

}

