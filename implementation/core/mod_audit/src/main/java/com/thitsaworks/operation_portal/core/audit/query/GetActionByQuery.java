package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.audit.data.UserData;

public interface GetActionByQuery {

    record Input(UserId userId) {}

    record Output(UserData userData) {}

    Output execute(Input input) throws Exception;

}
