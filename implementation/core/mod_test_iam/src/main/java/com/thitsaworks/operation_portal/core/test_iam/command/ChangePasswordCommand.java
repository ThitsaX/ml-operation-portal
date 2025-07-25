package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface ChangePasswordCommand {
    Output execute(Input input) throws IAMException;

    record Input(UserId userId, String currentPassword, String newPassword) { }

    record Output(AccessKey accessKey, String secretKey) {

    }
}
