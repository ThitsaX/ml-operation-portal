package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface GrantMenuActionCommand {

    Output execute(Input input) throws IAMException;

    record Input(String menuName, ActionCode action) { }

    record Output(boolean resultCode) { }

}


