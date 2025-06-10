package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllHubUser extends
        AbstractAuditableUseCase<GetAllHubUser.Input, GetAllHubUser.Output> {

    @Getter
    @NoArgsConstructor
    public static class Input {

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private List<HubUserInfo> userInfoList;

        @Value
        public static class HubUserInfo implements Serializable {

            private HubUserId hubUserId;

            private String name;

            private Email email;

            private String firstName;

            private String lastName;

            private String jobTitle;

            private Instant createdDate;

        }

    }

}
