package com.thitsaworks.operation_portal.dfsp_portal.hubuser.query;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.identity.HubUserId;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetHubUsers {

    @Value
    class Input {

    }

    @Value
    class Output {

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

    Output execute(Input input);

}
