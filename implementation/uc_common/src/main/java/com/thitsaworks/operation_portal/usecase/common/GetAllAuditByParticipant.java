package com.thitsaworks.operation_portal.usecase.common;

import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.misc.usecase.AbstractOwnableUseCase;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public abstract class GetAllAuditByParticipant
        extends AbstractOwnableUseCase<GetAllAuditByParticipant.Input, GetAllAuditByParticipant.Output> {

    public record Input(
            RealmId realmId,
            Instant fromDate,
            Instant toDate) {


    }

    public record Output(List<AuditInfo> auditInfoList) {

        public record AuditInfo(String userName, String actionName, Instant actionDate) implements Serializable {


        }

    }
}
