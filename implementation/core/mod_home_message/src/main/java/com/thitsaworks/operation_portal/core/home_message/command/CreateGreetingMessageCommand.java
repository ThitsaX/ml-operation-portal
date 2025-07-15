package com.thitsaworks.operation_portal.core.home_message.command;

public interface CreateGreetingMessageCommand {

    Output execute(Input input);
    record Input(String greetingTitle,
                 String greetingDetail){}

    record Output(boolean created){}

}
