package com.thitsaworks.operation_portal.iam.domain.command;

import com.thitsaworks.operation_portal.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.identity.PrincipalId;
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
