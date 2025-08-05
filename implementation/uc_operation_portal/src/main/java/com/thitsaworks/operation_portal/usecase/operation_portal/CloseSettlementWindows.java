package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;

public interface CloseSettlementWindows
    extends UseCase<CloseSettlementWindows.Input, CloseSettlementWindows.Output> {

    public record Input(
        String state,
        String reason,
        int settlementWindowId

    ) implements Serializable { }

    public record Output(

        int settlementWindowId,
        String state,
        String reason,
        String createdDate,
        String changedDate
    ) implements Serializable { }

}
