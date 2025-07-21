package com.thitsaworks.operation_portal.core.hubuser.command;

public interface RemoveGreetingCommand {

    Output execute(Input input);

    record Input() {}

    record Output(boolean removed) {}

}


