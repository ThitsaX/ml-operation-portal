package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;

import java.io.Serializable;
import java.util.List;

public interface GetSettlementsByParams
        extends UseCase<GetSettlementsByParams.Input, GetSettlementsByParams.Output> {

    public record Input(String currency,
                        Integer participantId,
                        Integer settlementWindowId,
                        Integer accountId,
                        String state,
                        String fromDateTime,
                        String toDateTime,
                        String fromSettlementWindowDateTime,
                        String toSettlementWindowDateTime
    ) implements Serializable {}

    public record Output(
            List<Settlement> settlementList
    ) implements Serializable {}

}
