package com.thitsaworks.operation_portal.core.audit.command;

import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.core.audit.exception.AuditException;

public interface CreateExceptionAuditCommand {

    Output execute(Input input) throws AuditException;

    record Input(AuditId auditId,
                 String exception) { }

    record Output() { }

}
