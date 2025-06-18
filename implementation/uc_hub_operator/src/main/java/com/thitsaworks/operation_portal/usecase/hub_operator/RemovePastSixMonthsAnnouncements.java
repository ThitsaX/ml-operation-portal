package com.thitsaworks.operation_portal.usecase.hub_operator;

import com.thitsaworks.operation_portal.component.usecase.AbstractOwnableUseCase;

public abstract class RemovePastSixMonthsAnnouncements extends
        AbstractOwnableUseCase<RemovePastSixMonthsAnnouncements.Input, RemovePastSixMonthsAnnouncements.Output> {

    public record Input() {}

    public record Output(boolean removed) {}

}
