package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GetAnnouncementById extends
                                         UseCase<GetAnnouncementById.Input, GetAnnouncementById.Output> {

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
