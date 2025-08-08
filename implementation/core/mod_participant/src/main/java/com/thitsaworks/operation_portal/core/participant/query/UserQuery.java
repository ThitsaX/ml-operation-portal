package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;
import java.util.Optional;

public interface UserQuery {

    List<UserData> getUsers(ParticipantId participantId);

    UserData get(UserId userId) throws ParticipantException;

    Optional<UserData> find(UserId userId);

    UserData get(Email email) throws ParticipantException;

}

