package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface RevokePrincipalActionCommand {

    Output execute(Input input) throws IAMException;

    record Input(PrincipalId principalId,
                 ActionId actionId) { }

    record Output(boolean revoked) { }

}

