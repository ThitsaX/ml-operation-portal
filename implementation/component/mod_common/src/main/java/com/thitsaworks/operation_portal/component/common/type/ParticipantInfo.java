package com.thitsaworks.operation_portal.component.common.type;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;

import java.io.Serializable;

public record ParticipantInfo(ParticipantId participantId, ParticipantName participantName, String description)
        implements Serializable {

}
