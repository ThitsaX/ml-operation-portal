package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.GrantId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.model.RoleGrant;

import java.io.Serializable;
import java.util.Objects;

public record RoleGrantData(GrantId grantId,
                            RoleId roleId,
                            ActionId actionId) implements Serializable {

    public RoleGrantData(RoleGrant roleGrant) {

        this(roleGrant.getGrantId(),
             roleGrant.getRole()
                      .getId(),
             roleGrant.getAction()
                      .getId());
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleGrantData that = (RoleGrantData) o;
        return Objects.equals(grantId, that.grantId);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(grantId);
    }


}
