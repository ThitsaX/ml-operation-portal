package com.thitsaworks.operation_portal.core.hubuser.command;

import com.thitsaworks.operation_portal.core.hubuser.exception.AlreadyAnnouncedException;
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
