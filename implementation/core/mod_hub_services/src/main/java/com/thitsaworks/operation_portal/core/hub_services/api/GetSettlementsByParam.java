package com.thitsaworks.operation_portal.core.hub_services.api;

import com.thitsaworks.operation_portal.core.hub_services.support.Settlement;

import java.util.List;

public class GetSettlementsByParam {

    public record Request() {}

    public record Response(List<Settlement> settlementList) {}

}

