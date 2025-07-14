package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface UpdateGreeting extends UseCase<UpdateGreeting.Input ,UpdateGreeting.Output> {
    record Input (GreetingId greetingId,
                  String greetingTitle,
                  String greetingDetail){}

    record Output(GreetingId greetingId){}
}
