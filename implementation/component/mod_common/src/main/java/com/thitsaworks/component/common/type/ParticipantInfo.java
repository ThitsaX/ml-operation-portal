package com.thitsaworks.component.common.type;

import com.thitsaworks.component.common.identifier.ParticipantId;

import java.io.Serializable;

public record ParticipantInfo(ParticipantId participantId, DfspCode dfsp_code, String name)
        implements Serializable {

}
