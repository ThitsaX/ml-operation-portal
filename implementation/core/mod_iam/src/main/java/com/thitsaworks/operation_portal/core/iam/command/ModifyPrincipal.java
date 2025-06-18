package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ModifyPrincipal {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private PrincipalId principalId;

        private UserRoleType userRoleType;

        private PrincipalStatus principalStatus;

        public Input(PrincipalId principalId, PrincipalStatus principalStatus) {

            this.principalId = principalId;
            this.principalStatus = principalStatus;
        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Output {

        private PrincipalId principalId;

        private boolean modified;

    }

    ModifyPrincipal.Output execute(ModifyPrincipal.Input input) throws PrincipalNotFoundException;

}
