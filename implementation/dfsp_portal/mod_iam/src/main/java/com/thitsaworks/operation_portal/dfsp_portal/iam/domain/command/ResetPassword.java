package com.thitsaworks.operation_portal.dfsp_portal.iam.domain.command;

import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ResetPassword {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private PrincipalId principalId;

        private String password;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Output {

        private AccessKey accessKey;

        private String secretKey;

        private boolean updated;

    }

    Output execute(Input input) throws PrincipalNotFoundException;

}
