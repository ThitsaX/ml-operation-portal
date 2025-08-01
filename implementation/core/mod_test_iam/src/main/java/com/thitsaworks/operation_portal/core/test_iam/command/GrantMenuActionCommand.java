package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface GrantMenuActionCommand {

    Output execute(Input input) throws IAMException;

    record Input(String menuName,
                 ActionCode action) { }

    record Output(boolean resultCode) { }

}


