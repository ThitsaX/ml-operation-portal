package com.thitsaworks.operation_portal.core.hub_services.api;

public class PostCloseSettlementWindows {



    public record Request(String state, String reason) { }

    public record Response(int settlementWindowId,
                           String state,
                           String reason,
                           String createdDate,
                           String closedDate,
                           String changedDate) {

    }

}

