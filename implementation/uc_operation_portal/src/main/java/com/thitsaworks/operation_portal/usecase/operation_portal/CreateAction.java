package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface CreateAction extends UseCase<CreateAction.Input, CreateAction.Output> {

    record Input(ActionCode actionCode,
                 String scope,
                 String description) { }

    record Output(ActionId actionId) { }

}
