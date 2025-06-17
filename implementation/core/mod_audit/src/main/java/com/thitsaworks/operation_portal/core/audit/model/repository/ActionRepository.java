package com.thitsaworks.operation_portal.core.audit.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.core.audit.model.Action;
import com.thitsaworks.operation_portal.core.audit.model.QAction;
import com.thitsaworks.component.common.identifier.ActionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, ActionId>, QuerydslPredicateExecutor<Action> {

    class Filters {

        public static BooleanExpression withActionName(String name) {

            return QAction.action.name.eq(name);

        }

    }

}
