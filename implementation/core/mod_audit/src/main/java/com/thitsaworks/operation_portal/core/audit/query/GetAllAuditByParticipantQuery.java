package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
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
                 UserId userId,
                 ActionId actionId,
                 List<ActionId> grantedActionList) { }

    record Output(List<AuditInfo> auditInfoList) {

        public record AuditInfo(Instant date,
                                String action,
                                Email madeBy) implements Serializable { }

    }

}
