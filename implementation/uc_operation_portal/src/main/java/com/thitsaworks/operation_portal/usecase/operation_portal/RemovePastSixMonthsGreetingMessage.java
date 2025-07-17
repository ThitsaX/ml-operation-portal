package com.thitsaworks.operation_portal.usecase.core_services;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemovePastSixMonthsGreetingMessage extends UseCase<RemovePastSixMonthsGreetingMessage.Input,RemovePastSixMonthsGreetingMessage.Output> {

    public record Input() {}

    public record Output(boolean removed) {}

}

