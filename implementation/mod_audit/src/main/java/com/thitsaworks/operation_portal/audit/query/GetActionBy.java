package com.thitsaworks.operation_portal.audit.query;

import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.audit.query.data.UserData;
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
