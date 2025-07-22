package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;

import java.io.Serializable;

public record ParticipantUserData(ParticipantUserId participantUserId,
                                  ParticipantId participantId,
                                  ParticipantName participantName,
                                  String name,
                                  Email email,
                                  String firstName,
                                  String lastName,
                                  String jobTitle,
                                  Long createdDate) implements Serializable {

    public ParticipantUserData(ParticipantUser participantUser) {

        this(participantUser.getParticipantUserId(),

             participantUser.getParticipant().getParticipantId(),

             participantUser.getParticipant().getParticipantName(),

             participantUser.getName(),

             participantUser.getEmail(),

             participantUser.getFirstName(),

             participantUser.getLastName(),

             participantUser.getJobTitle(),

             participantUser.getCreatedAt().getEpochSecond());
    }

}
