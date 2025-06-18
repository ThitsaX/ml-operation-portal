package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import java.time.Instant;

public abstract class CreateNewAnnouncement extends
        AbstractAuditableUseCase<CreateNewAnnouncement.Input, CreateNewAnnouncement.Output> {

    public static record Input(String announcementTitle, String announcementDetail, Instant announcementDate) {}

    public static record Output(boolean created) {}

}
