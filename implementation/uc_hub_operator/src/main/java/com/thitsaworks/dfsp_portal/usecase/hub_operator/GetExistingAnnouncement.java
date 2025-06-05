package com.thitsaworks.dfsp_portal.usecase.hub_operator;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public abstract class GetExistingAnnouncement extends
        AbstractAuditableUseCase<GetExistingAnnouncement.Input, GetExistingAnnouncement.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private AnnouncementId announcementId;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private AnnouncementId announcementId;

        private String announcementTitle;

        private String announcementDetail;

        private Instant announcementDate;

        private boolean isDeleted;

        private Instant createdDate;

    }

}
