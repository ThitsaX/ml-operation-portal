package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GetActionListByRole
    extends UseCase<GetActionListByRole.Input, GetActionListByRole.Output> {

    record Input(RoleId roleId) { }

    record Output(List<ActionOption> actionOptionList) {

        public record ActionOption(ActionId actionId,
                                   String actionName,
                                   boolean selected,
                                   boolean mandatory) { }

    }

}
