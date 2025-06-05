package com.thitsaworks.dfsp_portal.hubuser.query;

import com.thitsaworks.dfsp_portal.hubuser.exception.AnnouncementNotFoundException;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import lombok.Value;

import java.time.Instant;

public interface GetAnnouncement {

    @Value
    class Input {

        private AnnouncementId announcementId;

    }

    @Value
    class Output {

        private AnnouncementId announcementId;

        private String announcementTitle;

        private String announcementDetail;

        private Instant announcementDate;

        private boolean isDeleted;

        private Instant createdDate;

    }

    Output execute(Input input) throws AnnouncementNotFoundException;

}
