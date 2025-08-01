package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAuditByParticipantList
    extends UseCase<GetAuditByParticipantList.Input, GetAuditByParticipantList.Output> {

    record Input(RealmId realmId,
                 Instant fromDate,
                 Instant toDate,
                 UserId userId,
                 String actionName){ }

    record Output(List<AuditInfo> auditInfoList) {

        public record AuditInfo(String userName,
                                String actionName,
                                Instant actionDate) implements Serializable {

        }

    }

}
