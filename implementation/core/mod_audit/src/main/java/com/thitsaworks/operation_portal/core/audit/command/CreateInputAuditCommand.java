package com.thitsaworks.operation_portal.core.audit.command;

import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;

public interface CreateInputAuditCommand {

    Output execute(Input input);

    record Input(String actionName,
                 UserId actionBy,
                 RealmId realmId,
                 String inputInfo) { }

    record Output(AuditId auditId) { }

}
