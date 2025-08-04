package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface ResetUserPasswordCommand {
    record Input(
        UserId userId,
        String password) { }

    record Output(AccessKey accessKey,
                    String secretKey,
                    boolean updated) { }

    Output execute(Input input) throws IAMException;

}

