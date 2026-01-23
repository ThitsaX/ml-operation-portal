package com.thitsaworks.operation_portal.core.audit.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.TraceId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.core.audit.model.Audit;

public record AuditData(AuditId auditId,
                        ActionId actionId,
                        UserId userId,
                        RealmId realmId,
                        TraceId traceId,
                        String inputInfo,
                        String outputInfo,
                        String exceptionInfo) {

    public AuditData(Audit audit) {

        this(audit.getAuditId(),
             audit.getActionId(),
             audit.getUserId(),
             audit.getRealmId(),
             audit.getTraceId(),
             audit.getInputInfo(),
             audit.getOutputInfo(),
             audit.getException());
    }

}
