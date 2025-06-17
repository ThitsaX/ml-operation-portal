package com.thitsaworks.operation_portal.core.hubuser.data;

import com.thitsaworks.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.core.hubuser.model.Announcement;

import java.time.Instant;

public record AnnouncementData(AnnouncementId announcementId,

                               String announcementTitle,

                               String announcementDetail,

                               Instant announcementDate,

                               boolean isDeleted) {

    public AnnouncementData(Announcement announcement) {

        this(announcement.getAnnouncementId(),

             announcement.getAnnouncementTitle(),

             announcement.getAnnouncementDetail(),

             announcement.getAnnouncementDate(),

             announcement.isDeleted());
    }

}
