package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.component.common.identifier.AuditId;

public interface GetAuditDetailByIdQuery {

    Output execute(Input input);

    record Input(AuditId auditId) {

    }

    record Output(String inputInfo,
                  String outputInfo) {

    }

}
