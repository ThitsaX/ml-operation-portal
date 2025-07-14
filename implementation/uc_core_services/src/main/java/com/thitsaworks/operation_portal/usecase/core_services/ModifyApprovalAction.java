package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface ModifyApprovalAction extends UseCase<ModifyApprovalAction.Input, ModifyApprovalAction.Output> {

    record Input(ApprovalRequestId approvalRequestId,
                 ApprovalActionType action,
                 UserId respondedBy) { }

    record Output(ApprovalRequestId approvalRequestId) { }

}
