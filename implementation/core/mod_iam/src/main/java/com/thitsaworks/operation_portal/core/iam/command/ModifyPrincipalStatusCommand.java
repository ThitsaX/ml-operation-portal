package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface ModifyPrincipalStatusCommand {

    Output execute(Input input) throws IAMException;

    record Input(PrincipalId principalId,
                 PrincipalStatus principalStatus) { }

    record Output(PrincipalId principalId,
                  boolean modified) { }

}
