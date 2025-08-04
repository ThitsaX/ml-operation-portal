package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface CreatePrincipalCommand {

    Output execute(Input input) throws IAMException;

    record Input(PrincipalId principalId,
                 RealmType realmType,
                 String passwordPlain,
                 RealmId realmId,
                 UserRoleType userRoleType,
                 PrincipalStatus principalStatus) { }

    record Output(AccessKey accessKey, String secretKey) { }

}
