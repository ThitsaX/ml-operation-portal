package com.thitsaworks.operation_portal.core.test_iam.data;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.test_iam.model.User;

import java.io.Serializable;

public record UserData(UserId userId,

                       AccessKey accessKey,

                       String secretKey,

                       RealmType realmType,

                       RealmId realmId,

                       UserRoleType userRoleType,

                       PrincipalStatus principalStatus) implements Serializable {

    public UserData(User user) {

        this(user.getUserId(),

             user.getAccessKey(),

             user.getSecretKey(),

             user.getRealmType(),

             user.getRealmId(),

             user.getUserRoleType(),

             user.getPrincipalStatus());
    }

}
