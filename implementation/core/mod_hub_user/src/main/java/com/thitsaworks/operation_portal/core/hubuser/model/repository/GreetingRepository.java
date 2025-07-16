package com.thitsaworks.operation_portal.core.hubuser.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.hubuser.model.GreetingMessage;
import com.thitsaworks.operation_portal.core.hubuser.model.QGreetingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GreetingRepository extends JpaRepository<GreetingMessage, GreetingId>,
                                            QuerydslPredicateExecutor<GreetingMessage> {

    class Filters {

        private static final QGreetingMessage greeting = QGreetingMessage.greetingMessage;

        public static BooleanExpression findByGreetingTitle(String greetingTitle) {

            return greeting.greetingTitle.eq(greetingTitle);
        }

    }

}
