package com.thitsaworks.operation_portal.usecase.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thitsaworks.operation_portal.audit.identity.UserId;
import com.thitsaworks.operation_portal.component.http.jackson.InstantToLong;
import com.thitsaworks.operation_portal.component.http.jackson.LongToInstant;
import com.thitsaworks.operation_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.operation_portal.iam.identity.RealmId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllAudit
        extends AbstractAuditableUseCase<GetAllAudit.Input, GetAllAudit.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private RealmId realmId;

        private UserId userId;

        private Instant fromDate;

        private Instant toDate;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private List<AuditInfo> auditInfoList;

        @Value
        public static class AuditInfo implements Serializable {

            private String participantName;

            private String userName;

            private String actionName;

            private String inputInfo;

            private String outputInfo;

            @JsonSerialize(using = InstantToLong.class)
            @JsonDeserialize(using = LongToInstant.class)
            private Instant actionDate;

        }

    }
}
