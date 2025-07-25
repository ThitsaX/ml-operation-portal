package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.core.test_iam.model.QRole;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RoleRepository extends JpaRepository<Role, RoleId>, QuerydslPredicateExecutor<Role> {

    class Filters {

        public static BooleanExpression withName(String name){

            return QRole.role.name.equalsIgnoreCase(name);
        }

        public static BooleanExpression withRoleId(RoleId roleId){

            return QRole.role.roleId.eq(roleId);
        }

        public static BooleanExpression withoutRoleId(RoleId roleId){

            return QRole.role.roleId.ne(roleId);
        }

    }
}
