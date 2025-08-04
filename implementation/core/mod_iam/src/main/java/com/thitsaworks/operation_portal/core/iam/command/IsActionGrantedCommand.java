package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface IsActionGrantedCommand {
    Output execute(Input input) throws IAMException;

    record Input(PrincipalId principalId, ActionCode actionCode) { }

    record Output(boolean granted) { }

}
