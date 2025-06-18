package com.thitsaworks.operation_portal.core.hubuser.command;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.core.hubuser.exception.AlreadyAnnouncedException;
import com.thitsaworks.operation_portal.core.hubuser.exception.AnnouncementNotFoundException;

import java.time.Instant;

public interface ModifyAnnouncement {

    record Input(
            AnnouncementId announcementId,
            String announcementTitle,
            String announcementDetail,
            Instant announcementDate,
            boolean isDeleted) {}

    record Output(
            AnnouncementId announcementId,
            boolean modified) {}

    ModifyAnnouncement.Output execute(ModifyAnnouncement.Input input)
            throws AnnouncementNotFoundException, AlreadyAnnouncedException;

}
