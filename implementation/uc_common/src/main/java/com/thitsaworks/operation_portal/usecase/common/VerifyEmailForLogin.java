package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;

public abstract class VerifyEmailForLogin extends
                                          AbstractOwnableUseCase<VerifyEmailForLogin.Input, VerifyEmailForLogin.Output> {

    public record Input() {}

    public record Output() {}

}
