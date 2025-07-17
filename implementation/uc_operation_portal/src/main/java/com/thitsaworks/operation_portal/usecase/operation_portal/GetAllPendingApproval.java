package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.component.common.type.ApprovalActionType;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface GetAllPendingApproval extends UseCase<GetAllPendingApproval.Input, GetAllPendingApproval.Output> {

    record Input() { }

    record Output(List<PendingApproval> pendingApprovalList) {

        public record PendingApproval(ApprovalRequestId approvalRequestId,
                                      String requestedAction,
                                      String dfsp,
                                      String currency,
                                      BigDecimal amount,
                                      String requestedBy,
                                      Instant requestedDateTime,
                                      ApprovalActionType action) { }

    }

}
