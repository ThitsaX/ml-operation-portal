package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

public abstract class ChangeCurrentPassword extends
                                            AbstractAuditableUseCase<ChangeCurrentPassword.Input, ChangeCurrentPassword.Output> {

    public record Input(PrincipalId principalId, String oldPassword, String newPassword) {}

    public record Output(AccessKey accessKey, String secretKey) {}

}
