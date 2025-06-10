package com.thitsaworks.operation_portal.hubuser.query;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.hubuser.exception.HubUserNotFoundException;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;
import lombok.Value;

import java.time.Instant;

public interface GetHubUser {

    @Value
    class Input {

        private HubUserId hubUserId;

    }

    @Value
    class Output {

        private HubUserId hubUserId;

        private String name;

        private Email email;

        private String firstName;

        private String lastName;

        private String jobTitle;

        private Instant createdDate;

    }

    Output execute(Input input) throws HubUserNotFoundException;

}
