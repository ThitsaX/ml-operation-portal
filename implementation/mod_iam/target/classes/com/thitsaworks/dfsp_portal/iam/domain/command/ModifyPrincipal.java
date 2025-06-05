package com.thitsaworks.dfsp_portal.iam.domain.command;

import com.thitsaworks.dfsp_portal.iam.domain.UserRoleType;
import com.thitsaworks.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.dfsp_portal.iam.type.PrincipalStatus;
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
            this.principalId=principalId;
            this.principalStatus=principalStatus;
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
