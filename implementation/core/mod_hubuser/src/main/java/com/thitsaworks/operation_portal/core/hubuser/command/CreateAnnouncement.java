package com.thitsaworks.operation_portal.core.hubuser.command;

import com.thitsaworks.operation_portal.core.hubuser.exception.AlreadyAnnouncedException;

import java.time.Instant;

public interface CreateAnnouncement {

    record Input(String announcementTitle,
                 String announcementDetail,
                 Instant announcementDate) {}

    record Output(boolean created) {}

    Output execute(Input input) throws AlreadyAnnouncedException;

}
