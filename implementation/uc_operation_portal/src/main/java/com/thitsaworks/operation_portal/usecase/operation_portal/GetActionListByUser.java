package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GetActionListByUser extends UseCase<GetActionListByUser.Input, GetActionListByUser.Output> {

    record Input() { }

    record Output(List<Action> actionNames) {

        public record Action(ActionId actionId,
                             String actionName) { }

    }

}
