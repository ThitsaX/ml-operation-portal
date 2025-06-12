package com.thitsaworks.operation_portal.dfsp_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.dfsp_portal.hubuser.exception.HubUserNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.identity.HubUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface ModifyHubUser {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private HubUserId hubUserId;

        private String name;

        private String firstName;

        private String lastName;

        private String jobTitle;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private HubUserId hubUserId;

        private boolean modified;

    }

    Output execute(Input input) throws HubUserNotFoundException;

}
