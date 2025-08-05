package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.time.Instant;

public interface CreateGreetingMessageCommand {

    Output execute(Input input) throws ParticipantException;
    record Input(String greetingTitle,
                 String greetingDetail,
                 Instant greetingDate){}

    record Output(boolean created){}

}
