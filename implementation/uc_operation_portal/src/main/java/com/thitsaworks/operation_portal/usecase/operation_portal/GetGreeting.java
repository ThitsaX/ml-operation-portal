package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface GetGreeting extends UseCase<GetGreeting.Input ,GetGreeting.Output> {
    record Input(GreetingId greetingId){ }

    record Output(GreetingId greetingId,
                  String greetingTitle,
                  String greetingDetail,
                  boolean isDeleted ){}
}
