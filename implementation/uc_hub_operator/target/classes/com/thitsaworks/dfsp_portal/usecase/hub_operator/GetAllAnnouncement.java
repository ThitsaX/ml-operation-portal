package com.thitsaworks.dfsp_portal.usecase.hub_operator;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllAnnouncement extends
        AbstractAuditableUseCase<GetAllAnnouncement.Input, GetAllAnnouncement.Output> {

    @Getter
    @NoArgsConstructor
    public static class Input {

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private List<AnnouncementInfo> announcementInfoList;

        @Value
        public static class AnnouncementInfo implements Serializable {

            private AnnouncementId announcementId;

            private String announcementTitle;

            private String announcementDetail;

            private Instant announcementDate;

        }

    }

}
