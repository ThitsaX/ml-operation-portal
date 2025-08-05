package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.GrantId;
import com.thitsaworks.operation_portal.core.test_iam.model.UserGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserGrantRepository extends JpaRepository<UserGrant, GrantId>,
                                             QuerydslPredicateExecutor<UserGrant> {

    class Filters{

    }
}
