package com.thitsaworks.operation_portal.core.hubuser.query;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.core.hubuser.data.GreetingData;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;

import java.util.List;

public interface GreetingQuery {

    List<GreetingData>getGreeting();

    GreetingData get(GreetingId greetingId) throws HubUserException;
}
