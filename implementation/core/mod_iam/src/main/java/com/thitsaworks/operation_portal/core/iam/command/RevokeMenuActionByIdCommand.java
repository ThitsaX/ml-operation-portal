package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface RevokeMenuActionByIdCommand {

    Output execute(Input input) throws IAMException;

    record Input(MenuId menuId,
                 ActionId actionId) { }

    record Output(boolean revoked) { }

}

