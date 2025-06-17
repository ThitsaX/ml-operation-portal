package com.thitsaworks.operation_portal.core.iam.domain.command;

import com.thitsaworks.operation_portal.core.iam.domain.SecurityToken;
import com.thitsaworks.operation_portal.core.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.component.common.identifier.PrincipalId;
import lombok.Value;

public interface Authenticate {

    @Value
    class Input {

        private PrincipalId principalId;

        private String passwordPlain;

    }

    @Value
    class Output {

        private SecurityToken securityToken;

    }

    Output execute(Input input)
            throws PasswordAuthenticationFailureException, PrincipalNotFoundException;

}
