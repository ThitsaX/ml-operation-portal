package com.thitsaworks.operation_portal.dfsp_portal.iam.domain.command;

import com.thitsaworks.operation_portal.dfsp_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.dfsp_portal.iam.exception.DuplicatePrincipalException;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.dfsp_portal.iam.identity.RealmId;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.dfsp_portal.iam.type.RealmType;
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

        private PrincipalStatus activeStatus;

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
