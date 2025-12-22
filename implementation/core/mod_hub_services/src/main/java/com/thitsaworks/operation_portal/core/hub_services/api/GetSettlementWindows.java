package com.thitsaworks.operation_portal.core.hub_services.api;

import com.thitsaworks.operation_portal.core.hub_services.support.SettlementContent;

import java.util.List;

public class GetSettlementWindows {

    public record Request() { }

    public record SettlementWindow(
        Integer settlementWindowId,
        String state,
        String reason,
        String createdDate,
        String changedDate,
        String closedDate,
        List<SettlementContent> content
    ) { }

}




