package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.TraceId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public interface GetAuditListByParticipant
    extends UseCase<GetAuditListByParticipant.Input, GetAuditListByParticipant.Output> {

    record Input(Instant fromDate,
                 Instant toDate,
                 UserId userId,
                 ActionId actionid,
                 Integer page,
                 Integer pageSize) { }

    record Output(List<AuditInfo> auditInfoList,
                  Long total,
                  Integer totalPages
    ) {

        public record AuditInfo(AuditId auditId,
                                Instant date,
                                String action,
                                Email madeBy,
                                TraceId traceId) implements Serializable {

        }

    }

}
