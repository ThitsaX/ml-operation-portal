package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAnnouncementInfoList {

    record Input() { }

    record Output(List<AnnouncementInfo> announcementInfoList) {

        public record AnnouncementInfo(
            AnnouncementId announcementId,
            String announcementTitle,
            String announcementDetail,
            Instant announcementDate
        ) implements Serializable { }

    }

    Output execute(Input input);

}
