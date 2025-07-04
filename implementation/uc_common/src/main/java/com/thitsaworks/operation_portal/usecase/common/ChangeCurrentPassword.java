package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ChangeCurrentPassword extends
                                       UseCase<ChangeCurrentPassword.Input, ChangeCurrentPassword.Output> {

    record Input(PrincipalId principalId, String oldPassword, String newPassword) { }

    record Output(AccessKey accessKey, String secretKey) { }

}
