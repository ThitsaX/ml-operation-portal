package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public abstract class ResetCurrentPassword extends
                                           AbstractAuditableUseCase<ResetCurrentPassword.Input, ResetCurrentPassword.Output> {

    public record Input(Email email, String password) {


    }

    public record Output(AccessKey accessKey, String secretKey, Boolean updated) {


    }

}
