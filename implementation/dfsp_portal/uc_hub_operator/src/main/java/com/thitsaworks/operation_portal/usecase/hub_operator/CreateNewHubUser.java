package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;
import com.thitsaworks.operation_portal.iam.domain.UserRoleType;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class CreateNewHubUser
        extends AbstractAuditableUseCase<CreateNewHubUser.Input, CreateNewHubUser.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String name;

        private Email email;

        private String password;

        private String firstName;

        private String lastName;

        private String jobTitle;

        private UserRoleType userRoleType;

        private PrincipalStatus activeStatus;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private HubUserId hubUserId;

        private AccessKey accessKey;

        private String secretKey;

    }

}
