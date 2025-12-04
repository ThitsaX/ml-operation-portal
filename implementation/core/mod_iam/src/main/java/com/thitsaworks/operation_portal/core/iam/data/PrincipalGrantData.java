package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.GrantId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalGrant;

import java.util.Objects;

public record PrincipalGrantData(GrantId grantId,
                                 PrincipalId principalId,
                                 ActionId actionId) {

    public PrincipalGrantData(PrincipalGrant principalGrant) {

        this(principalGrant.getGrantId(),
             principalGrant.getPrincipal()
                           .getPrincipalId(),
             principalGrant.getAction()
                           .getActionId());
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrincipalGrantData that = (PrincipalGrantData) o;
        return Objects.equals(grantId, that.grantId);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(grantId);
    }

}
