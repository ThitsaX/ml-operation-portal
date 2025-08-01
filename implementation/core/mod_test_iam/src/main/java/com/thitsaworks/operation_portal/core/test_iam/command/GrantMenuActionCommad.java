package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.MenuId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface GrantMenuActionCommad {
    Output execute(Input input) throws IAMException;

    record Input(MenuId menuId,
                 ActionId actionId) { }

    record Output(boolean resultCode) { }

}


