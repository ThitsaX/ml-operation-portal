package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface GetNetTransferAmountByWindowId
    extends UseCase<GetNetTransferAmountByWindowId.Input, GetNetTransferAmountByWindowId.Output> {

    public record Input(

        int settlementWindowId

    ) implements Serializable { }

    public record Output(

        int settlementWindowId,
        String windowOpenedDate,
        String windowClosedDate,
        List<Detail> details

    ) implements Serializable { }

    public record Detail(
        String participantName,
        BigDecimal debitAmount,
        BigDecimal creditAmount,
        String currency
    ) implements Serializable {}

}
