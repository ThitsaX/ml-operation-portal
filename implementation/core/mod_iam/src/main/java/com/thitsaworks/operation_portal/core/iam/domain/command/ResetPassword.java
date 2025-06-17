package com.thitsaworks.operation_portal.core.iam.domain.command;

import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.PrincipalId;
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
