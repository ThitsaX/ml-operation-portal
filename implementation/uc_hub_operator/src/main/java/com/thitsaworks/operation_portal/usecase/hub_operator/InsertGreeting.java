package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface InsertGreeting extends UseCase<InsertGreeting.Input, InsertGreeting.Output> {

    record Input(String greetingTitle,
                 String greetingDetail) { }

    record Output(boolean created) { }

}
