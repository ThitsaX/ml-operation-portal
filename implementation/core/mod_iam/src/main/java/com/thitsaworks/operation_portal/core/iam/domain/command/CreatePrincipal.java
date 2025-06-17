package com.thitsaworks.operation_portal.core.iam.domain.command;

import com.thitsaworks.component.common.type.RealmType;
import com.thitsaworks.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.iam.exception.DuplicatePrincipalException;
import com.thitsaworks.component.common.identifier.AccessKey;
import com.thitsaworks.component.common.identifier.PrincipalId;
import com.thitsaworks.component.common.identifier.RealmId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreatePrincipal {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private PrincipalId principalId;

        private RealmType realmType;

        private String passwordPlain;

        private RealmId realmId;

        private UserRoleType userRoleType;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Output {

        private AccessKey accessKey;

        private String secretKey;

    }

    CreatePrincipal.Output execute(CreatePrincipal.Input input) throws DuplicatePrincipalException;

}
