package com.thitsaworks.operation_portal.core.approval.command;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;

import java.math.BigDecimal;

public interface CreateApprovalCommand {

    Output execute(Input input);

    record Input(String requestedAction,
                 String dfsp,
                 String currency,
                 BigDecimal amount,
                 UserId requestedBy) { }

    record Output(ApprovalRequestId approvalRequestId) { }

}

