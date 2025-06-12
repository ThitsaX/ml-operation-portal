package com.thitsaworks.operation_portal.usecase.participant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thitsaworks.operation_portal.component.http.jackson.InstantToLong;
import com.thitsaworks.operation_portal.component.http.jackson.LongToInstant;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllParticipantUser extends
        AbstractAuditableUseCase<GetAllParticipantUser.Input, GetAllParticipantUser.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private ParticipantId participantId;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private List<UserInfo> userInfoList;

        @Value
        public static class UserInfo implements Serializable {

            private ParticipantUserId participantUserId;

            private String name;

            private Email email;

            private String firstName;

            private String lastName;

            private String jobTitle;

            private String roleType;

            private String status;

            @JsonSerialize(using = InstantToLong.class)
            @JsonDeserialize(using = LongToInstant.class)
            private Instant createdDate;

        }

    }

}
