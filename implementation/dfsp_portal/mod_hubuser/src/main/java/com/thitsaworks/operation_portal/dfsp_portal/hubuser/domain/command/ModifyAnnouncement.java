package com.thitsaworks.operation_portal.dfsp_portal.hubuser.domain.command;

import com.thitsaworks.operation_portal.dfsp_portal.hubuser.exception.AnnouncementNotFoundException;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.exception.AlreadyAnnouncedException;
import com.thitsaworks.operation_portal.dfsp_portal.hubuser.identity.AnnouncementId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public interface ModifyAnnouncement {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private AnnouncementId announcementId;

        private String announcementTitle;

        private String announcementDetail;

        private Instant announcement_date;

        private boolean isDeleted;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private AnnouncementId announcementId;

        private boolean modified;

    }

    ModifyAnnouncement.Output execute(ModifyAnnouncement.Input input)
            throws AnnouncementNotFoundException, AlreadyAnnouncedException;

}
