package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.core.iam.exception.PasswordAuthenticationFailureException;
import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;

public interface ChangePassword {

    record Input (PrincipalId principalId, String oldPassword, String newPassword) {
    }

    record Output( AccessKey accessKey, String secretKey) {

    }

    Output execute(Input input) throws PasswordAuthenticationFailureException, PrincipalNotFoundException;

}
