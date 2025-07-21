package com.thitsaworks.operation_portal.core.hubuser.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.hubuser.data.GreetingData;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.QGreetingMessage;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.GreetingRepository;
import com.thitsaworks.operation_portal.core.hubuser.query.GreetingQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class GreetingQueryHandler implements GreetingQuery {

    private final GreetingRepository greetingRepository;

    private final QGreetingMessage greeting = QGreetingMessage.greetingMessage;

    @Override
    public List<GreetingData> getGreeting() {


        return this.greetingRepository.findAll()
                                      .stream()
                                      .map(Greeting-> new GreetingData(Greeting.getId()
                                          ,Greeting.getGreetingTitle(),
                                                                       Greeting.getGreetingDetail(),
                                                                       Greeting.isDeleted()))
                                      .collect(Collectors.toList());
    }


    @Override
    public GreetingData get(GreetingId greetingId) throws HubUserException {
        BooleanExpression predicate = this.greeting.greetingId.eq(greetingId);


        return this.greetingRepository.findOne(predicate)
                                      .map(Greeting -> new GreetingData(Greeting.getId(),
                                                                        Greeting.getGreetingTitle(),
                                                                        Greeting.getGreetingDetail(),
                                                                        Greeting.isDeleted()))
                                      .orElseThrow(() -> new HubUserException(HubUserErrors.GREETING_NOT_FOUND));

    }
}