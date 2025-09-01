package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface GetActionListByUserId extends UseCase<GetActionListByUserId.Input, GetActionListByUserId.Output> {

    record Input(){}

    record Output(List<ActionName> actionNames){
        public record ActionName(ActionId actionId,
                                 String actionName){}
    }
}
