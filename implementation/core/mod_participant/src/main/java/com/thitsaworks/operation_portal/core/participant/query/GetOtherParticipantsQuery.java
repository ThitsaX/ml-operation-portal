package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.type.ParticipantInfo;

import java.util.List;

public interface GetOtherParticipantsQuery {

    List<ParticipantInfo> getOtherParticipants(ParticipantId participantId);



}
