package com.thitsaworks.operation_portal.core.home_message.command;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.home_message.exception.GreetingException;

public interface UpdateGreetingCommand {

    Output execute(Input input) throws GreetingException;

    record Input (GreetingId greetingId,
                  String greetingTitle,
                  String greetingDetail){}

    record Output(GreetingId greetingId){}
}
