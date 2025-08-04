package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.model.Role;

import java.io.Serializable;

public record RoleData(RoleId roleId,
                       String name,
                       boolean active) implements Serializable {

    public RoleData(Role role) {

        this(role.getRoleId(),
             role.getName(),
             role.getActive());
    }

}
