package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface RemoveRoleFromPrincipalCommand {

    Output execute(Input input) throws IAMException;

    record Input(PrincipalId principalId, RoleId roleId) { }

    record Output(boolean removed) { }

}

