package com.thitsaworks.operation_portal.core.iam.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalRoleId;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalRole;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipalRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PrincipalRoleRepository extends JpaRepository<PrincipalRole, PrincipalRoleId>,
                                                 QuerydslPredicateExecutor<PrincipalRole> {

    class Filters {

        public static BooleanExpression byPrincipalId(PrincipalId principalId) {
            return  QPrincipalRole.principalRole.principal.principalId.eq(principalId);
        }

    }

}
