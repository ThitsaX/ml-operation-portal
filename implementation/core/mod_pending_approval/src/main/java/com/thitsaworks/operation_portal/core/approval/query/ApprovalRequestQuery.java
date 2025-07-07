package com.thitsaworks.operation_portal.core.approval.query;

import com.thitsaworks.operation_portal.core.approval.data.ApprovalRequestData;

import java.util.List;

public interface ApprovalRequestQuery {

    List<ApprovalRequestData> getPendingApprovalRequests();

}
