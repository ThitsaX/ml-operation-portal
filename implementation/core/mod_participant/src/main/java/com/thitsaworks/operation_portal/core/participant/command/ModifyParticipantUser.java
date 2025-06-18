package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;

public interface ModifyParticipantUser {

    record Input(
            ParticipantUserId participantUserId,
            String name,
            Email email,
            String firstName,
            String lastName,
            String jobTitle,
            ParticipantId participantId) {}

    record Output(ParticipantUserId participantUserId, boolean modified) {}

    ModifyParticipantUser.Output execute(ModifyParticipantUser.Input input) throws ParticipantUserNotFoundException;
}
