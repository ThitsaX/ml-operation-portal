package com.thitsaworks.operation_portal.core.participant.command;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;

import java.time.Instant;

public interface ModifyAnnouncementCommand {

    record Input(
            AnnouncementId announcementId,
            String announcementTitle,
            String announcementDetail,
            Instant announcementDate,
            boolean isDeleted) {}

    record Output(
            AnnouncementId announcementId,
            boolean modified) {}

    ModifyAnnouncementCommand.Output execute(ModifyAnnouncementCommand.Input input)
            throws ParticipantException;

}
