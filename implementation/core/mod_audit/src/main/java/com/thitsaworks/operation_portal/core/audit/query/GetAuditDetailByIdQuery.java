package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.core.audit.data.AuditData;
import com.thitsaworks.operation_portal.core.audit.exception.AuditException;

public interface GetAuditDetailByIdQuery {

    Output execute(Input input) throws AuditException;

    record Input(AuditId auditId) { }

    record Output(AuditData auditData) { }

}
