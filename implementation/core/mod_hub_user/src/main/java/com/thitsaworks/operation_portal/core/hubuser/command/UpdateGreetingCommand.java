package com.thitsaworks.operation_portal.core.hubuser.command;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;

import java.time.Instant;

public interface UpdateGreetingCommand {

    Output execute(Input input) throws HubUserException;

    record Input (GreetingId greetingId,
                  String greetingTitle,
                  String greetingDetail,
                  boolean isDeleted,
                  Instant isDelete){}

    record Output(GreetingId greetingId){}
}
