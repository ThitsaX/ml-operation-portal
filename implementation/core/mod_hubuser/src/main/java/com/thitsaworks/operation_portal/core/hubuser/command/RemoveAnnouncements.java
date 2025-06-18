package com.thitsaworks.operation_portal.core.hubuser.command;

public interface RemoveAnnouncements {

    record Input() {}

    record Output(boolean removed) {}

    RemoveAnnouncements.Output execute(RemoveAnnouncements.Input input);

}
