package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

import java.util.List;

public interface GrantRoleActionsCommand {

    Output execute(Input input) throws IAMException;

    record Input(String role,
                 List<ActionCode> actionCodeList) { }

    record Output(boolean resultCode) { }

}
