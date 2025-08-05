package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.time.Instant;

public interface CreateAnnouncementCommand {

    record Input(String announcementTitle,
                 String announcementDetail,
                 Instant announcementDate) {}

    record Output(boolean created) {}

    Output execute(Input input) throws ParticipantException;

}
