package com.thitsaworks.operation_portal.core.iam.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.RoleId;
import com.thitsaworks.operation_portal.core.iam.model.QRole;
import com.thitsaworks.operation_portal.core.iam.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
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
