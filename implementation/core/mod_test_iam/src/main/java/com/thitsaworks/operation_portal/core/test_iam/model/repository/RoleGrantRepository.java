package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.GrantId;
import com.thitsaworks.operation_portal.core.test_iam.model.RoleGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleGrantRepository extends JpaRepository<RoleGrant, GrantId>, QuerydslPredicateExecutor<RoleGrant> {
}
