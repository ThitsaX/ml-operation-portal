package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class GetExistingHubUser
        extends AbstractAuditableUseCase<GetExistingHubUser.Input, GetExistingHubUser.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private HubUserId hubUserId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private HubUserId hubUserId;

        private String name;

        private Email email;

        private String firstName;

        private String lastName;

        private String jobTitle;

        private Long createdDate;

    }

}
