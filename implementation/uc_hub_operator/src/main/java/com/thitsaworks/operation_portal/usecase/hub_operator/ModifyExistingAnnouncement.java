package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface ModifyExistingAnnouncement extends
                                            UseCase<ModifyExistingAnnouncement.Input, ModifyExistingAnnouncement.Output> {

    public record Input(
            AnnouncementId announcementId,
            String announcementTitle,
            String announcementDetail,
            Instant announcementDate,
            boolean isDeleted
    ) {}

    public record Output(
            AnnouncementId announcementId,
            boolean modified
    ) {}

}
