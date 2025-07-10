package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAllAuditByParticipant
    extends UseCase<GetAllAuditByParticipant.Input, GetAllAuditByParticipant.Output> {

    record Input(RealmId realmId,
                 Instant fromDate,
                 Instant toDate) { }

    record Output(List<AuditInfo> auditInfoList) {

        public record AuditInfo(String userName,
                                String actionName,
                                Instant actionDate) implements Serializable {

        }

    }

}
