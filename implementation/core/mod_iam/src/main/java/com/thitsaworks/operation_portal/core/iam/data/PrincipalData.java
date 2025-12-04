package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.core.iam.model.Principal;

import java.io.Serializable;
import java.util.Objects;

public record PrincipalData(PrincipalId principalId,
                            AccessKey accessKey,
                            String secretKey,
                            RealmId realmId,
                            PrincipalStatus principalStatus) implements Serializable {

    public PrincipalData(Principal principal) {

        this(principal.getPrincipalId(),
             principal.getAccessKey(),
             principal.getSecretKey(),
             principal.getRealmId(),
             principal.getPrincipalStatus());
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrincipalData that = (PrincipalData) o;
        return Objects.equals(principalId, that.principalId);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(principalId);
    }


}
