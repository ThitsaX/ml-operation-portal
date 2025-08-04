package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.QIAMAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface IAMActionRepository extends JpaRepository<IAMAction, ActionId>, QuerydslPredicateExecutor<IAMAction> {

    class Filters {

        public static BooleanExpression withActionCode(ActionCode actionCode) {

            return QIAMAction.iAMAction.actionCode.eq(actionCode);
        }

        public static BooleanExpression withScope(String scope) {

            return QIAMAction.iAMAction.scope.eq(scope);
        }

    }

}
