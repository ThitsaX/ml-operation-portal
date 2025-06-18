package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.hubuser.identity.AnnouncementId;

import java.time.Instant;

public abstract class GetExistingAnnouncement extends
        AbstractAuditableUseCase<GetExistingAnnouncement.Input, GetExistingAnnouncement.Output> {

    public record Input(
            AnnouncementId announcementId
    ) {}

    public record Output(
            AnnouncementId announcementId,
            String announcementTitle,
            String announcementDetail,
            Instant announcementDate,
            boolean isDeleted,
            Instant createdDate
    ) {}

}
