package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.core.iam.exception.PrincipalNotFoundException;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;

public interface ResetPassword {

    record Input(
            PrincipalId principalId,
            String password) {


    }

    record Output(            AccessKey accessKey,
            String secretKey,
            boolean updated) {


    }

    Output execute(Input input) throws PrincipalNotFoundException;

}
