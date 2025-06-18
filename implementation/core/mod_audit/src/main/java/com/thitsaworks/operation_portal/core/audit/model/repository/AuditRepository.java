package com.thitsaworks.operation_portal.core.audit.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.AuditId;
import com.thitsaworks.operation_portal.core.audit.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository  extends JpaRepository<Audit, AuditId>,
        QuerydslPredicateExecutor<Audit>  {
}
