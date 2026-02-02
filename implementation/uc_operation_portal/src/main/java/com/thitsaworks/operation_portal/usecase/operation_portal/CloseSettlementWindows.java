package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thitsaworks.operation_portal.component.fspiop.model.ErrorInformation;
import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

public interface CloseSettlementWindows
    extends UseCase<CloseSettlementWindows.Input, CloseSettlementWindows.Output> {

    public record Input(
        String state,
        String reason,
        int settlementWindowId

    ) implements Serializable { }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private int settlementWindowId;

        private String state;

        private String reason;

        private String createdDate;

        private String closedDate;

        private String changedDate;

        private ErrorInformation errorInformation;

    }

}
