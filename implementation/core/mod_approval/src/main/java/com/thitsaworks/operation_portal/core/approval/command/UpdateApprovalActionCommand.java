package com.thitsaworks.operation_portal.core.approval.command;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.core.approval.exception.ApprovalException;

public interface UpdateApprovalActionCommand {

    Output execute(Input input) throws ApprovalException;

    record Input(ApprovalRequestId approvalRequestId,
                 ApprovalActionType action,
                 UserId respondedBy) { }

    record Output(ApprovalRequestId approvalRequestId) { }

}
