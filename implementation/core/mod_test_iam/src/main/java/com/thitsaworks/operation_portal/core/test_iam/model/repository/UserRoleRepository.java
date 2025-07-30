package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.UserRoleId;
import com.thitsaworks.operation_portal.core.test_iam.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> ,
                                            QuerydslPredicateExecutor<UserRole> {
    class Filters{

    }
}
