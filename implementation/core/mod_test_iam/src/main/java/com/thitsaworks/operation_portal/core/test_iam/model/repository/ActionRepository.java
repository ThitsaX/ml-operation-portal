package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.model.Action;
import com.thitsaworks.operation_portal.core.test_iam.model.QAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ActionRepository extends JpaRepository<Action, ActionId>, QuerydslPredicateExecutor<Action> {

    class Filters {

        public static BooleanExpression withActionCode(ActionCode actionCode) {

            return QAction.action.actionCode.eq(actionCode);
        }

        public static BooleanExpression withScope(String scope) {

            return QAction.action.scope.eq(scope);
        }

    }

}
