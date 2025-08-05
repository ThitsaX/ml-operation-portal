package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalRoleId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface AssignRoleToUser extends UseCase<AssignRoleToUser.Input, AssignRoleToUser.Output> {

    record Input(PrincipalId principalId, RoleId roleId) { }

    record Output(PrincipalRoleId principalRoleId) { }

}
