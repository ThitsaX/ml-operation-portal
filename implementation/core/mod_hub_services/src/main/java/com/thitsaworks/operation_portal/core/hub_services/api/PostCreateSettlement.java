package com.thitsaworks.operation_portal.core.hub_services.api;

import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindow;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowId;

import java.util.List;

public class PostCreateSettlement {

    public record Request(
            String settlementModel,
            String reason,
            List<SettlementWindowId> settlementWindows
    ) {}

    public record Response(
            Integer id,
            String state,
            List<SettlementWindow> settlementWindows,
            List<SettlementParticipant> participants
    ) {}

}

