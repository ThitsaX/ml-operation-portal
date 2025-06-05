package com.thitsaworks.dfsp_portal.usecase.hub_operator;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractOwnableUseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class RemovePastSixMonthsAnnouncements extends
        AbstractOwnableUseCase<RemovePastSixMonthsAnnouncements.Input, RemovePastSixMonthsAnnouncements.Output> {

    @Getter
    @NoArgsConstructor
    public static class Input {

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private boolean removed;

    }

}
