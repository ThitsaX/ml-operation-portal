package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface ModifyUserCommand {
    Output execute(Input input) throws IAMException;

    record Input(UserId userId,
                 UserRoleType userRoleType,
                 PrincipalStatus principalStatus) { }

    record Output(UserId userId,
                  boolean modified) {
    }


}
