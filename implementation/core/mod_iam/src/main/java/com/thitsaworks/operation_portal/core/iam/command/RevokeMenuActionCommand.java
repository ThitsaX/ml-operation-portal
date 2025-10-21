package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface RevokeMenuActionCommand {

    Output execute(Input input) throws IAMException;

    record Input(String menuName,
                 ActionCode actionCode) { }

    record Output(boolean revoked) { }

}

