package com.thitsaworks.operation_portal.core.audit.command;

import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.core.audit.exception.AuditNotFoundException;

public interface CreateOutputAuditCommand {

    Output execute(Input input) throws AuditNotFoundException;

    record Input(AuditId auditId,
                 String outputInfo) { }

    record Output() { }

}
