package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface FinalizeSettlement
    extends UseCase<FinalizeSettlement.Input, FinalizeSettlement.Output> {

    record Input(Integer settlementId) { }

    record Output(int settlementId,
                  String settlementWindowIds,
                  String windowOpenedDate,
                  String windowClosedDate,
                  List<Detail> details

    ) implements Serializable { }

    record Detail(String participantName,
                  BigDecimal participantLimit,
                  BigDecimal participantBalance,
                  BigDecimal debitAmount,
                  BigDecimal creditAmount,
                  String currency,
                  String participantSettlementCurrencyId
    ) implements Serializable { }

}
