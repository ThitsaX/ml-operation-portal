package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.participant.data.GreetingData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;

public interface GreetingQuery {

    List<GreetingData>getGreeting();

    GreetingData get(GreetingId greetingId) throws ParticipantException;
}
