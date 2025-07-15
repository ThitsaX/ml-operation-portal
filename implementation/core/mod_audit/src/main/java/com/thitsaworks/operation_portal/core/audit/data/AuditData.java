package com.thitsaworks.operation_portal.core.audit.data;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;

public record AuditData(AuditId auditId,
                        ActionId actionId,
                        UserId userId,
                        RealmId realmId,
                        String inputInfo,
                        String outputInfo) {
}
