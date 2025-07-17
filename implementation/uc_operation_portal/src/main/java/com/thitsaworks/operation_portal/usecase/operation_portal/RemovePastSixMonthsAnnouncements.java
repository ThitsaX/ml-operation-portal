package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RemovePastSixMonthsAnnouncements extends
                                                  UseCase<RemovePastSixMonthsAnnouncements.Input, RemovePastSixMonthsAnnouncements.Output> {

    public record Input() {}

    public record Output(boolean removed) {}

}
