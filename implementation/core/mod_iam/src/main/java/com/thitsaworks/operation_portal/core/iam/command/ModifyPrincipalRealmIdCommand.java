package com.thitsaworks.operation_portal.core.iam.command;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;

public interface ModifyPrincipalRealmIdCommand {

    Output execute(Input input) throws IAMException;

    record Input(PrincipalId principalId,
                 RealmId realmId) { }

    record Output(PrincipalId principalId,
                  boolean modified) { }

}
