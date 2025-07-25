package com.thitsaworks.operation_portal.core.approval.query;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.core.approval.data.ApprovalRequestData;
import com.thitsaworks.operation_portal.core.approval.exception.ApprovalException;

import java.util.List;

public interface ApprovalRequestQuery {

    List<ApprovalRequestData> getPendingApprovalRequests();
    
    ApprovalRequestData getPendingApprovalRequestByID(ApprovalRequestId approvalRequestId) throws ApprovalException;

}
