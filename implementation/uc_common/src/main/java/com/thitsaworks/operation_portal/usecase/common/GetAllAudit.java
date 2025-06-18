package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractAuditableUseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllAudit
        extends AbstractAuditableUseCase<GetAllAudit.Input, GetAllAudit.Output> {

    public record Input(
            RealmId realmId,
            UserId userId,
            Instant fromDate,
            Instant toDate) {}

    public record Output(List<AuditInfo> auditInfoList) {

        public record AuditInfo(String participantName,
                         String userName,
                         String actionName,
                         String inputInfo,
                         String outputInfo,
                         Instant actionDate) implements Serializable {


        }

    }
}
