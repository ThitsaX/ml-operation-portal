package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.usecase.AbstractOwnableUseCase;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class VerifyEmailForLogin extends
        AbstractOwnableUseCase<VerifyEmailForLogin.Input, VerifyEmailForLogin.Output> {

    @Getter
    @NoArgsConstructor
    public static class Input {

    }

    @Getter
    @NoArgsConstructor
    public static class Output {

    }

}
