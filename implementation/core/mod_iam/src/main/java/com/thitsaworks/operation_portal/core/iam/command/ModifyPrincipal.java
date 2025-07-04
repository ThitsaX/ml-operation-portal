package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface ModifyPrincipal {

    record Input(PrincipalId principalId,
                 UserRoleType userRoleType,
                 PrincipalStatus principalStatus) {

        public Input(PrincipalId principalId, PrincipalStatus principalStatus) {

            this(principalId, null, principalStatus);
        }

    }

    record Output(PrincipalId principalId,
                  boolean modified) {
    }

    ModifyPrincipal.Output execute(ModifyPrincipal.Input input) throws IAMException;

}
