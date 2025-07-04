package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface VerifyEmailForLogin extends
                                     UseCase<VerifyEmailForLogin.Input, VerifyEmailForLogin.Output> {

    record Input() { }

    record Output() { }

}
