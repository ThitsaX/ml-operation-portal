package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.time.Instant;

public interface UpdateGreetingCommand {

    Output execute(Input input) throws ParticipantException;

    record Input (GreetingId greetingId,
                  String greetingTitle,
                  String greetingDetail,
                  boolean isDeleted,
                  Instant isDelete){}

    record Output(GreetingId greetingId){}
}
