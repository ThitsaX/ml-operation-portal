package com.thitsaworks.operation_portal.core.audit.command;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;

public interface CreateAction {

    record Input(String name) {
    }

    record Output(boolean created, ActionId actionId) {
    }

    Output execute(Input input);

}
