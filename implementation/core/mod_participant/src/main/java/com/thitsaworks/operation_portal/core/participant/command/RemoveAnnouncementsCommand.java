package com.thitsaworks.operation_portal.core.participant.command;

public interface RemoveAnnouncementsCommand {

    record Input() {}

    record Output(boolean removed) {}

    RemoveAnnouncementsCommand.Output execute(RemoveAnnouncementsCommand.Input input);

}
