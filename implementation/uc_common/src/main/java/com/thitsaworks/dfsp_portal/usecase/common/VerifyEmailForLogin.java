package com.thitsaworks.dfsp_portal.usecase.common;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractOwnableUseCase;
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
