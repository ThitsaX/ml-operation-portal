package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.math.BigDecimal;

public interface CreateApprovalRequest extends UseCase<CreateApprovalRequest.Input, CreateApprovalRequest.Output> {

    record Input(String requestedAction,
                 String participant,
                 String participantCurrency,
                 String participantCurrencyId,
                 BigDecimal amount,
                 UserId requestedBy) { }

    record Output(ApprovalRequestId approvalRequestId) { }

}
