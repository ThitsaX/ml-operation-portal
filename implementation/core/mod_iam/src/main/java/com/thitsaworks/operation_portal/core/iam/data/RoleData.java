package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.model.Role;

import java.io.Serializable;
import java.util.Objects;

public record RoleData(RoleId roleId,
                       String name,
                       boolean active,
                       boolean isDfsp) implements Serializable {

    public RoleData(Role role) {

        this(role.getRoleId(),
             role.getName(),
             role.getActive(),
             role.getIsDfsp());
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleData that = (RoleData) o;
        return Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(roleId);
    }

}
