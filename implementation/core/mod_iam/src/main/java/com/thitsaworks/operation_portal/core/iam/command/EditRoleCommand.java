package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface EditRoleCommand {

    Output execute(Input input) throws IAMException;

    record Input(RoleId roleId, String name) { }

    record Output(boolean resultCode) { }

}

