package com.thitsaworks.operation_portal.core.approval.data;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.core.approval.model.ApprovalRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ApprovalRequestData {

    private ApprovalRequestId approvalRequestId;

    private String fundInOutAction;

    private String participantName;

    private String currency;

    private String participantCurrencyId;

    private BigDecimal amount;

    private UserId requestedBy;

    private UserId respondedBy;

    private Instant requestedDtm;

    private ApprovalActionType action;

    public ApprovalRequestData() { }

    public ApprovalRequestData(ApprovalRequest request) {

        this.approvalRequestId = request.getApprovalRequestId();
        this.fundInOutAction = request.getRequestedAction();
        this.participantName = request.getParticipantName();
        this.currency = request.getParticipantCurrency();
        this.participantCurrencyId = request.getParticipantCurrencyId();
        this.amount = request.getAmount();
        this.requestedBy = request.getRequestedBy();
        this.respondedBy = request.getRespondedBy();
        this.requestedDtm = request.getRequestedDtm();
        this.action = request.getAction();
    }

}
