package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import org.luaj.vm2.ast.Str;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface GetNetTransferAmountBySettlementId
    extends UseCase<GetNetTransferAmountBySettlementId.Input, GetNetTransferAmountBySettlementId.Output> {

    public record Input(

        int settlementId

    ) implements Serializable { }

    public record Output(

        int settlementId,
        String settlementWindowIds,
        String windowOpenedDate,
        String windowClosedDate,
        List<Detail> details

    ) implements Serializable { }

    public record Detail(

        String participantName,
        BigDecimal participantLimit,
        BigDecimal participantBalance,
        BigDecimal ndcPercent,
        BigDecimal debitAmount,
        BigDecimal creditAmount,
        String currency,
        String participantSettlementCurrencyId
    ) implements Serializable { }

}
