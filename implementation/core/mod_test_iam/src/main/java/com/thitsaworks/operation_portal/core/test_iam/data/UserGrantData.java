package com.thitsaworks.operation_portal.core.test_iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.core.test_iam.model.UserGrant;

public record UserGrantData(GrantId grantId,
                            UserId userId,
                            ActionId actionId) {
    public UserGrantData(UserGrant userGrant){
        this(userGrant.getGrantId(),
             userGrant.getUser()
                      .getUserId(),
             userGrant.getIAMAction()
                      .getActionId());
    }
}
