package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
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
                                Email madeBy) implements Serializable { }

    }

}
