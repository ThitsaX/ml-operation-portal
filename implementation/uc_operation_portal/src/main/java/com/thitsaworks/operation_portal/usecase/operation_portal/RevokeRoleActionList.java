package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface RevokeRoleActionList extends UseCase<RevokeRoleActionList.Input, RevokeRoleActionList.Output> {

    record Input(String roleName,
                 List<ActionCode> actionCodeList) { }

    record Output(boolean revoked) { }

}

