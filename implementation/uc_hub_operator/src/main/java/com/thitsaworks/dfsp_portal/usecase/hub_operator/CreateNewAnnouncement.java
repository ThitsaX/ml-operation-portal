package com.thitsaworks.dfsp_portal.usecase.hub_operator;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

public abstract class CreateNewAnnouncement extends
        AbstractAuditableUseCase<CreateNewAnnouncement.Input, CreateNewAnnouncement.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private String announcementTitle;

        private String announcementDetail;

        private Instant announcementDate;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {

        private boolean created;

    }

}
