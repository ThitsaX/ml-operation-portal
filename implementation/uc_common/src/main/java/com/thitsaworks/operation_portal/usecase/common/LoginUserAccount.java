package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.component.type.Email;

public abstract class LoginUserAccount extends
                                       AbstractAuditableUseCase<LoginUserAccount.Input, LoginUserAccount.Output> {

    public record Input(Email email, String passwordPlain) {}

    public record Output(AccessKey accessKey, String secretKey) {}

}
