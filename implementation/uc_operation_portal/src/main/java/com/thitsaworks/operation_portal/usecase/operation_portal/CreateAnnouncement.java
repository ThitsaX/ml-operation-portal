package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import java.time.Instant;

public interface CreateAnnouncement
    extends UseCase<CreateAnnouncement.Input, CreateAnnouncement.Output> {

    record Input(String announcementTitle,
                 String announcementDetail,
                 Instant announcementDate) { }

    record Output(boolean created) { }

}
