package com.thitsaworks.dfsp_portal.usecase.common;

import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class LoginUserAccount extends
        AbstractAuditableUseCase<LoginUserAccount.Input, LoginUserAccount.Output> {

    @Getter
    @AllArgsConstructor
    public static class Input {

        private Email email;

        private String passwordPlain;

    }

    @Getter
    @AllArgsConstructor
    public static class Output {

        private AccessKey accessKey;

        private String secretKey;
    }

}
