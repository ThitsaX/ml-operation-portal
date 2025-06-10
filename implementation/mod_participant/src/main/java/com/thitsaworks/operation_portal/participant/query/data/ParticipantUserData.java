package com.thitsaworks.operation_portal.participant.query.data;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.participant.domain.ParticipantUser;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.identity.ParticipantUserId;
import com.thitsaworks.operation_portal.participant.type.DfspCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantUserData implements Serializable {

    protected ParticipantUserId participantUserId;

    protected ParticipantId participantId;

    protected DfspCode dfspCode;

    protected String name;

    protected Email email;

    protected String firstName;

    protected String lastName;
    private String jobTitle;

    private Instant createdDate;

    public ParticipantUserData(ParticipantUser participantUser) {

        this.participantUserId = participantUser.getParticipantUserId();
        this.participantId = participantUser.getParticipant().getParticipantId();
        this.name = participantUser.getName();
        this.email = participantUser.getEmail();
        this.firstName = participantUser.getFirstName();
        this.lastName = participantUser.getLastName();
        this.jobTitle = participantUser.getJobTitle();

    }

}
