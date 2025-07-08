package com.thitsaworks.operation_portal.core.home_message.query;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.home_message.data.GreetingData;
import com.thitsaworks.operation_portal.core.home_message.exception.GreetingException;

import java.util.List;

public interface GreetingQuery {

    List<GreetingData>getGreeting();

    GreetingData get(GreetingId greetingId) throws GreetingException;
}
