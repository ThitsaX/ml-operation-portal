package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface GrantRoleActionByIdCommand {

    Output execute(Input input) throws IAMException;

    record Input(RoleId roleId, ActionId actionId) { }

    record Output(boolean resultCode) { }

}
