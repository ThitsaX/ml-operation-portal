package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.UserRoleId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface AssignRoleToUser extends UseCase<AssignRoleToUser.Input, AssignRoleToUser.Output> {

    record Input(UserId userId, RoleId roleId) { }

    record Output(UserRoleId userRoleId) { }

}
