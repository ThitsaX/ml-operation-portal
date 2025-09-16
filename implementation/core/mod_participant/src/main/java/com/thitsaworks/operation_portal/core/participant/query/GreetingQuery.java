package com.thitsaworks.operation_portal.core.participant.query;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.participant.data.GreetingData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.util.List;
import java.util.Optional;

public interface GreetingQuery {

    List<GreetingData> getGreeting();

    Optional<GreetingData> getLatestGreeting();

    GreetingData get(GreetingId greetingId) throws ParticipantException;

}
