package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface ModifyRoleGrantList
    extends UseCase<ModifyRoleGrantList.Input, ModifyRoleGrantList.Output> {

    record Input(RoleId roleId, List<ActionId> actionIdList) { }

    record Output(boolean modified) { }

}
