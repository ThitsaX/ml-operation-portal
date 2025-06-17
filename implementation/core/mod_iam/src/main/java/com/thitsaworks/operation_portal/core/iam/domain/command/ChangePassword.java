package com.thitsaworks.operation_portal.core.iam.domain.command;

import com.thitsaworks.operation_portal.core.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.PrincipalId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ChangePassword {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private PrincipalId principalId;

        private String oldPassword;

        private String newPassword;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Output {

        private AccessKey accessKey;

        private String secretKey;

    }

    Output execute(Input input) throws PasswordAuthenticationFailureException, PrincipalNotFoundException;

}
