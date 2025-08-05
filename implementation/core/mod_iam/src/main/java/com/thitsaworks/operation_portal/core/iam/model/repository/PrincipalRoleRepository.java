package com.thitsaworks.operation_portal.core.iam.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalRoleId;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PrincipalRoleRepository extends JpaRepository<PrincipalRole, PrincipalRoleId>,
                                                 QuerydslPredicateExecutor<PrincipalRole> {

    class Filters {

    }

}
