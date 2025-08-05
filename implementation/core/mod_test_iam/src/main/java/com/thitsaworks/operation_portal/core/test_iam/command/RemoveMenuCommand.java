package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.MenuId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface RemoveMenuCommand {
    Output execute(Input input) throws IAMException;

    record Input(MenuId menuId){}

    record Output(boolean success){}
}

