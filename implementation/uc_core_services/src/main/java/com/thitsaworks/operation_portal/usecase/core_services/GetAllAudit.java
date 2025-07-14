package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAllAudit extends UseCase<GetAllAudit.Input, GetAllAudit.Output> {

    record Input(RealmId realmId,
                 UserId userId,
                 Instant fromDate,
                 Instant toDate,
                 String actionName) { }

    record Output(List<AuditInfo> auditInfoList) {

        public record AuditInfo(String participantName,
                                String userName,
                                String actionName,
                                String inputInfo,
                                String outputInfo,
                                Instant actionDate) implements Serializable {

        }

    }

}
