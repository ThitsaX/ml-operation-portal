package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface CreateNewAnnouncement
    extends UseCase<CreateNewAnnouncement.Input, CreateNewAnnouncement.Output> {

    record Input(String announcementTitle,
                 String announcementDetail,
                 Instant announcementDate) { }

    record Output(boolean created) { }

}
