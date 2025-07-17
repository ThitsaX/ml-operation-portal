package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.time.Instant;

public interface CreateGreetingMessage extends UseCase<CreateGreetingMessage.Input, CreateGreetingMessage.Output> {

    record Input(String greetingTitle,
                 String greetingDetail,
                 Instant greetingDate) { }

    record Output(boolean created) { }

}
