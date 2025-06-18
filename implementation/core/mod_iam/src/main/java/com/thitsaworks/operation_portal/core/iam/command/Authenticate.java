package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.core.iam.model.SecurityToken;
import com.thitsaworks.operation_portal.core.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;

public interface Authenticate {

    record Input(PrincipalId principalId, String passwordPlain) {

    }

    record Output(SecurityToken securityToken) {

    }

    Output execute(Input input)
            throws PasswordAuthenticationFailureException, PrincipalNotFoundException;

}
