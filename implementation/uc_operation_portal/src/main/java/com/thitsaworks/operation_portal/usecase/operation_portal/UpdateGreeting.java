package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface UpdateGreeting extends UseCase<UpdateGreeting.Input ,UpdateGreeting.Output> {
    record Input (GreetingId greetingId,
                  String greetingTitle,
                  String greetingDetail,
                  boolean isDeleted,
                  Instant greetingDate){}

    record Output(GreetingId greetingId){}
}
