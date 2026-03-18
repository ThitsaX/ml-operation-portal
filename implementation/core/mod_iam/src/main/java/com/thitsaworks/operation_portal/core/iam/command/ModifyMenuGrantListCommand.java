package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

import java.util.List;

public interface ModifyMenuGrantListCommand {

    Output execute(Input input) throws IAMException;

    record Input(MenuId menuId, List<ActionId> actionIdList) { }

    record Output(boolean modified) { }

}
