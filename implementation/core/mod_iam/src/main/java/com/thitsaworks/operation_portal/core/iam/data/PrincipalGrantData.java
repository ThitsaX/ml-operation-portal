package com.thitsaworks.operation_portal.core.iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.GrantId;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalGrant;

public record PrincipalGrantData(GrantId grantId,
                                 PrincipalId principalId,
                                 ActionId actionId) {

    public PrincipalGrantData(PrincipalGrant principalGrant){

        this(principalGrant.getGrantId(),
             principalGrant.getPrincipal().getPrincipalId(),
             principalGrant.getAction()
                           .getActionId());
    }
}
