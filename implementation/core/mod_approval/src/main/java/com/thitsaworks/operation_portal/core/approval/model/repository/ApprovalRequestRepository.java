package com.thitsaworks.operation_portal.core.approval.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.ApprovalRequestId;
import com.thitsaworks.operation_portal.core.approval.model.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRequestRepository
    extends JpaRepository<ApprovalRequest, ApprovalRequestId>, QuerydslPredicateExecutor<ApprovalRequest> {
}
