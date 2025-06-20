package com.thitsaworks.operation_portal.usecase.hub_operator;


import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllAnnouncement extends
                                         AbstractAuditableUseCase<GetAllAnnouncement.Input, GetAllAnnouncement.Output> {

    public record Input() {}

    public record Output(List<AnnouncementInfo> announcementInfoList) {
        public record AnnouncementInfo(
                AnnouncementId announcementId,
                String announcementTitle,
                String announcementDetail,
                Instant announcementDate
        ) implements Serializable {}
    }
}
