package com.thitsaworks.dfsp_portal.hubuser.query;

import com.thitsaworks.dfsp_portal.hubuser.identity.AnnouncementId;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAnnouncements {

    @Value
    class Input {

    }

    @Value
    class Output {

        private List<AnnouncementInfo> announcementInfoList;

        @Value
        public static class AnnouncementInfo implements Serializable {

            private AnnouncementId announcementId;

            private String announcementTitle;

            private String announcementDetail;

            private Instant announcementDate;

            private boolean isDeleted;

        }

    }

    GetAnnouncements.Output execute(GetAnnouncements.Input input);

}
