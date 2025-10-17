package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GrantUserActionList extends UseCase<GrantUserActionList.Input, GrantUserActionList.Output> {

    record Input(PrincipalId principalId,
                 List<ActionId> actionIdList) { }

    record Output(boolean resultCode) { }

}
