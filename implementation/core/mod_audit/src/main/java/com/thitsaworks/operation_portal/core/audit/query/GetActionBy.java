package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.audit.data.UserData;
import lombok.Value;

public interface GetActionBy {

    @Value
    class Input {

        private UserId userId;

    }

    @Value
    class Output {

        private UserData userData;

    }

    Output execute(Input input) throws Exception;

}
