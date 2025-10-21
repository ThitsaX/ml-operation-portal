package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface RevokeMenuActionList extends UseCase<RevokeMenuActionList.Input, RevokeMenuActionList.Output> {

    record Input(String menuName,
                 List<ActionCode> actionCodeList) { }

    record Output(boolean revoked) { }

}
