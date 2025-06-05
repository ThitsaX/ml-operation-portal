package com.thitsaworks.dfsp_portal.usecase.common;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.dfsp_portal.iam.identity.PrincipalId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class ChangeCurrentPassword extends
        AbstractAuditableUseCase<ChangeCurrentPassword.Input, ChangeCurrentPassword.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private PrincipalId principalId;

        private String oldPassword;

        private String newPassword;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private AccessKey accessKey;

        private String secretKey;

    }

}
