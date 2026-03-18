package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

import java.util.List;

public interface ModifyRoleGrantListCommand {

    Output execute(Input input) throws IAMException;

    record Input(RoleId roleId, List<ActionId> actionIdList) { }

    record Output(boolean modified) { }

}
