package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.identifier.ParticipantUserId;
import com.thitsaworks.component.common.type.DfspCode;
import com.thitsaworks.component.common.type.PrincipalStatus;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;

import java.io.Serializable;
import java.time.Instant;

public record ParticipantUserData(ParticipantUserId participantUserId,
                                  ParticipantId participantId,
                                  DfspCode dfspCode,
                                  String name,
                                  Email email,
                                  String firstName,
                                  String lastName,
                                  String jobTitle,
                                  PrincipalStatus status,

                                  Long createdDate) implements Serializable {

    public ParticipantUserData(ParticipantUser participantUser) {

        this(participantUser.getParticipantUserId(),

             participantUser.getParticipant().getParticipantId(),

             participantUser.getParticipant().getDfspCode(),

             participantUser.getName(),

             participantUser.getEmail(),

             participantUser.getFirstName(),

             participantUser.getLastName(),

             participantUser.getJobTitle(),

             participantUser.getStatus(),

             participantUser.getCreatedAt().getEpochSecond());
    }

}
