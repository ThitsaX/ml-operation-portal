package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.MenuId;
import com.thitsaworks.operation_portal.core.test_iam.model.Menu;
import com.thitsaworks.operation_portal.core.test_iam.model.QMenu;
import com.thitsaworks.operation_portal.core.test_iam.model.QRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, MenuId>, QuerydslPredicateExecutor<Menu> {

    class Filters {

        public static BooleanExpression withName(String name){

            return QMenu.menu.name.equalsIgnoreCase(name);
        }
    }

}
