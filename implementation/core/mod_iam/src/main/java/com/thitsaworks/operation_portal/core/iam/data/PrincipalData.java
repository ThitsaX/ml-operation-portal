package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.iam.model.Principal;

import java.io.Serializable;

public record PrincipalData(PrincipalId principalId,

                            AccessKey accessKey,

                            String secretKey,

                            RealmType realmType,

                            RealmId realmId,

                            UserRoleType userRoleType,

                            PrincipalStatus principalStatus) implements Serializable {

    public PrincipalData(Principal principal) {

        this(principal.getPrincipalId(),

             principal.getAccessKey(),

             principal.getSecretKey(),

             principal.getRealmType(),

             principal.getRealmId(),

             principal.getUserRoleType(),

             principal.getPrincipalStatus());
    }

}
