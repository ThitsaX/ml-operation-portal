package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.AnnouncementId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface ModifyAnnouncement extends
                                    UseCase<ModifyAnnouncement.Input, ModifyAnnouncement.Output> {

    record Input(AnnouncementId announcementId,
                 String announcementTitle,
                 String announcementDetail,
                 Instant announcementDate,
                 boolean isDeleted
    ) { }

    record Output(
        AnnouncementId announcementId,
        boolean modified
    ) { }

}
