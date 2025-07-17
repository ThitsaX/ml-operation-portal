package com.thitsaworks.operation_portal.core.hubuser.command;

import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;

import java.time.Instant;

public interface CreateGreetingMessageCommand {

    Output execute(Input input) throws HubUserException;
    record Input(String greetingTitle,
                 String greetingDetail,
                 Instant greetingDate){}

    record Output(boolean created){}

}
