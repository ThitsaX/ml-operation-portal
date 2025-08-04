package com.thitsaworks.operation_portal.core.test_iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;

public interface CreateUserCommand {
    Output execute(Input input) throws IAMException;

    record Input(
        UserId userId,
        RealmType realmType,
        String passwordPlain,
        RealmId realmId,
        UserRoleType userRoleType,

        PrincipalStatus principalStatus) {


    }

    record Output(AccessKey accessKey, String secretKey) {

    }

}

