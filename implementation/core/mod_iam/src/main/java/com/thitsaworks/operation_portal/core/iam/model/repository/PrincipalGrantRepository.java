package com.thitsaworks.operation_portal.core.iam.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.GrantId;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PrincipalGrantRepository extends JpaRepository<PrincipalGrant, GrantId>,
                                                  QuerydslPredicateExecutor<PrincipalGrant> {

    class Filters {

    }

}
