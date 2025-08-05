package com.thitsaworks.operation_portal.core.test_iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalRoleId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.model.UserRole;

import java.io.Serializable;

public record UserRoleData(PrincipalRoleId principalRoleId,
                           RoleId roleId,
                           UserId userId) implements Serializable {
    public UserRoleData(UserRole userRole){
        this(userRole.getPrincipalRoleId(),
             userRole.getRole()
                     .getRoleId(),
             userRole.getUser()
                     .getUserId());


    }
}
