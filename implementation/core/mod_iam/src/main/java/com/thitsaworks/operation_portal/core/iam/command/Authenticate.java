package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.exception.IAMIgnorableException;
import com.thitsaworks.operation_portal.core.iam.model.SecurityToken;

public interface Authenticate {

    record Input(PrincipalId principalId, String passwordPlain) {

    }

    record Output(SecurityToken securityToken) {

    }

    Output execute(Input input) throws IAMException, IAMIgnorableException;

}
