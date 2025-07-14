package com.thitsaworks.operation_portal.core.home_message.query.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.home_message.data.GreetingData;
import com.thitsaworks.operation_portal.core.home_message.exception.GreetingErrors;
import com.thitsaworks.operation_portal.core.home_message.exception.GreetingException;
import com.thitsaworks.operation_portal.core.home_message.model.QGreeting;
import com.thitsaworks.operation_portal.core.home_message.model.repository.GreetingRepository;
import com.thitsaworks.operation_portal.core.home_message.query.GreetingQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class GreetingQueryHandler implements GreetingQuery {

    private final GreetingRepository greetingRepository;

    private final QGreeting greeting = QGreeting.greeting;

    @Override
    public List<GreetingData> getGreeting() {


        return this.greetingRepository.findAll()
                                      .stream()
                                      .map(Greeting-> new GreetingData(Greeting.getId()
                                          ,Greeting.getGreetingTitle(),
                                                                       Greeting.getGreetingDetail(),
                                                                       Greeting.getCreatedAt()))
                                      .collect(Collectors.toList());
    }


    @Override
    public GreetingData get(GreetingId greetingId) throws GreetingException {
        BooleanExpression predicate = this.greeting.greetingId.eq(greetingId);


        return this.greetingRepository.findOne(predicate)
                                      .map(Greeting -> new GreetingData(Greeting.getId(),
                                                                        Greeting.getGreetingTitle(),
                                                                        Greeting.getGreetingDetail(),
                                                                        Greeting.getCreatedAt()))
                                      .orElseThrow(() -> new GreetingException(GreetingErrors.GREETING_NOT_FOUND));

    }
}