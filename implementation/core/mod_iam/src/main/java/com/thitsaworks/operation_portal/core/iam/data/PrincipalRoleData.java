package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalRoleId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalRole;

import java.io.Serializable;

public record PrincipalRoleData(PrincipalRoleId principalRoleId,
                                RoleId roleId,
                                PrincipalId principalId) implements Serializable {

    public PrincipalRoleData(PrincipalRole principalRole) {

        this(principalRole.getPrincipalRoleId(),
             principalRole.getRole()
                          .getRoleId(),
             principalRole.getPrincipal()
                          .getPrincipalId());

    }

}
