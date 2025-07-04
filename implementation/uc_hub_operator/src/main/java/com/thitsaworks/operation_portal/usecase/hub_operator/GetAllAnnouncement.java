package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAllAnnouncement extends
                                    UseCase<GetAllAnnouncement.Input, GetAllAnnouncement.Output> {

    record Input() { }

    record Output(List<AnnouncementInfo> announcementInfoList) {

        public record AnnouncementInfo(
            AnnouncementId announcementId,
            String announcementTitle,
            String announcementDetail,
            Instant announcementDate
        ) implements Serializable { }

    }

}
