package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.core.participant.model.User;

import java.io.Serializable;

public record UserData(UserId userId,
                       ParticipantId participantId,
                       ParticipantName participantName,
                       String name,
                       Email email,
                       String firstName,
                       String lastName,
                       String jobTitle,
                       Long createdDate) implements Serializable {

    public UserData(User user) {

        this(user.getUserId(),

             user.getParticipant().getParticipantId(),

             user.getParticipant().getParticipantName(),

             user.getName(),

             user.getEmail(),

             user.getFirstName(),

             user.getLastName(),

             user.getJobTitle(),

             user.getCreatedAt().getEpochSecond());
    }

}
