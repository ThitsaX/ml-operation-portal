package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.TraceId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAllAuditByParticipantQuery {

    Output execute(Input input);

    record Input(RealmId realmId,
                 Instant fromDate,
                 Instant toDate,
                 List<ActionId> grantedActionList,
                 UserId userId,
                 ActionId actionId,
                 int page,
                 int size
    ) { }

    record Output(List<AuditInfo> auditInfoList,
                  long totalElements,
                  int totalPages
    ) {

        public record AuditInfo(AuditId auditId,
                                Instant date,
                                String action,
                                Email madeBy,
                                TraceId traceId) implements Serializable { }

    }

}
