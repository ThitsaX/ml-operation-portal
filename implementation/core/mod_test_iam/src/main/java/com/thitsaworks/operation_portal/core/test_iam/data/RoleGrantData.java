package com.thitsaworks.operation_portal.core.test_iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.model.RoleGrant;

import java.io.Serializable;

public record RoleGrantData(GrantId grantId,
                            RoleId roleId,
                            ActionId actionId) implements Serializable {
    public RoleGrantData(RoleGrant roleGrant){
        this(roleGrant.getGrantId(),
        roleGrant.getRole()
                 .getRoleId(),
        roleGrant.getIAMAction()
                 .getActionId());
    }
}
