package com.thitsaworks.operation_portal.core.home_message.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.home_message.model.Greeting;
import com.thitsaworks.operation_portal.core.home_message.model.QGreeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GreetingRepository extends JpaRepository<Greeting , GreetingId>,
                                            QuerydslPredicateExecutor<Greeting> {
    class Filters{
        private  static  final QGreeting greeting = QGreeting.greeting;

        public  static BooleanExpression findByGreetingTitle(String greetingTitle){
            return greeting.greetingTitle.eq(greetingTitle);
        }


    }
}
