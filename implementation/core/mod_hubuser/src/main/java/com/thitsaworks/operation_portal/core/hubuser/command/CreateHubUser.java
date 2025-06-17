package com.thitsaworks.operation_portal.core.hubuser.command;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.core.participant.exception.EmailAlreadyRegisteredException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreateHubUser {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private String name;

        private Email email;

        private String firstName;

        private String lastName;

        private String jobTitle;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean created;

        private HubUserId hubUserId;

    }

    Output execute(Input input) throws EmailAlreadyRegisteredException;
}
