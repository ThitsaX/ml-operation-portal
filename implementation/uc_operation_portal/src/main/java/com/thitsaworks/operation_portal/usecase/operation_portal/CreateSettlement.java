package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindow;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowId;

import java.io.Serializable;
import java.util.List;

public interface CreateSettlement
        extends UseCase<CreateSettlement.Input, CreateSettlement.Output> {

    public record Input(
            String settlementModel,
            String reason,
            List<SettlementWindowId> settlementWindowIdList
    ) implements Serializable {}

    public record Output(
            Integer settlementId,
            String settlementModel,
            String state,
            String reason,
            String createdDate,
            String changedDate,
            List<SettlementWindow> settlementWindowList,
            List<SettlementParticipant> settlementParticipantList
    ) implements Serializable {}

}
