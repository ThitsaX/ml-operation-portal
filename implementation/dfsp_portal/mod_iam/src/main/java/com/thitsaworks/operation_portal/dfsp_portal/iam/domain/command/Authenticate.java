package com.thitsaworks.operation_portal.dfsp_portal.iam.domain.command;

import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.SecurityToken;
import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
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
