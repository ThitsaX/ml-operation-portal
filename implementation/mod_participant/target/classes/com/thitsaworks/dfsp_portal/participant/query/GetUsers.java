package com.thitsaworks.dfsp_portal.participant.query;

import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantId;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetUsers {

    @Value
    class Input {

        private ParticipantId participantId;

    }

    @Value
    class Output {

        private List<UserInfo> userInfoList;

        @Value
        public static class UserInfo implements Serializable {

            private ParticipantUserId participantUserId;

            private String name;

            private Email email;

            private String firstName;

            private String lastName;

            private String jobTitle;

            private String userRoleType;

            private String status;

            private Instant createdDate;

        }

    }

    Output execute(Input input);

}
