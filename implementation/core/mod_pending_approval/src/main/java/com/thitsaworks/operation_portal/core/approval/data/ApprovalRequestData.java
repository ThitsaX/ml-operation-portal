package com.thitsaworks.operation_portal.core.approval.data;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.core.approval.model.ApprovalRequest;

import java.math.BigDecimal;
import java.time.Instant;

public record ApprovalRequestData(ApprovalRequestId approvalRequestId,
                                  String requestedAction,
                                  String dfsp,
                                  String currency,
                                  BigDecimal amount,
                                  UserId requestedBy,
                                  UserId respondedBy,
                                  Instant requestedDtm,
                                  ApprovalActionType action) {

    public ApprovalRequestData(ApprovalRequest request) {

        this(request.getApprovalRequestId(),
             request.getRequestedAction(),
             request.getDfsp(),
             request.getCurrency(),
             request.getAmount(),
             request.getRequestedBy(),
             request.getRespondedBy() == null ? null : request.getRespondedBy(),
             request.getRequestedDtm(),
             request.getAction());
    }

}
