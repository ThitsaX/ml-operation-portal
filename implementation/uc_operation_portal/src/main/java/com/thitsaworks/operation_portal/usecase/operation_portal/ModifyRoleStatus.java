package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifyRoleStatus extends UseCase<ModifyRoleStatus.Input, ModifyRoleStatus.Output> {

    record Input(RoleId roleId,
                 boolean active) { }

    record Output(boolean modified) { }

}
