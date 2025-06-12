package com.thitsaworks.operation_portal.dfsp_portal.participant.query;

import com.thitsaworks.operation_portal.dfsp_portal.participant.query.data.ParticipantData;

import java.util.List;
import java.util.Optional;

public interface ParticipantQuery {

    List<ParticipantData> getParticipants();

}

