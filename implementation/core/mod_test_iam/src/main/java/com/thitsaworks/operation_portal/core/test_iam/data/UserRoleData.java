package com.thitsaworks.operation_portal.core.test_iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.UserRoleId;
import com.thitsaworks.operation_portal.core.test_iam.model.UserRole;

import java.io.Serializable;

public record UserRoleData(UserRoleId userRoleId,
                           RoleId roleId,
                           UserId userId) implements Serializable {
    public UserRoleData(UserRole userRole){
        this(userRole.getUserRoleId(),
             userRole.getRole()
                     .getRoleId(),
             userRole.getUser()
                     .getUserId());


    }
}
