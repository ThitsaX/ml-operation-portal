package com.thitsaworks.dfsp_portal.hubuser.domain.command;

import com.thitsaworks.dfsp_portal.hubuser.exception.AlreadyAnnouncedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public interface CreateAnnouncement {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private String announcementTitle;

        private String announcementDetail;

        private Instant announcementDate;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean created;

    }

    Output execute(Input input) throws AlreadyAnnouncedException;

}
