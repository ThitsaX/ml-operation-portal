package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface GetAuditDetailById extends UseCase<GetAuditDetailById.Input, GetAuditDetailById.Output> {

    record Input(AuditId auditId) { }

    record Output(String inputInfo,
                  String outputInfo) { }

}
