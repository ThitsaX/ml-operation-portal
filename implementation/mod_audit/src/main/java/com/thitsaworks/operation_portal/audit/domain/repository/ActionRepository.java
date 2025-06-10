package com.thitsaworks.operation_portal.audit.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.audit.domain.Action;
import com.thitsaworks.operation_portal.audit.domain.QAction;
import com.thitsaworks.operation_portal.audit.identity.ActionId;
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
