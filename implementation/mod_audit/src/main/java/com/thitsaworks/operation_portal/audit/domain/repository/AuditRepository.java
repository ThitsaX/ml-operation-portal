package com.thitsaworks.operation_portal.audit.domain.repository;

import com.thitsaworks.operation_portal.audit.domain.Audit;
import com.thitsaworks.operation_portal.audit.identity.AuditId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository  extends JpaRepository<Audit, AuditId>,
        QuerydslPredicateExecutor<Audit>  {
}
