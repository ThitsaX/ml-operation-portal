package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface CreateGreetingMessage extends UseCase<CreateGreetingMessage.Input, CreateGreetingMessage.Output> {

    record Input(String greetingTitle,
                 String greetingDetail) { }

    record Output(boolean created) { }

}
