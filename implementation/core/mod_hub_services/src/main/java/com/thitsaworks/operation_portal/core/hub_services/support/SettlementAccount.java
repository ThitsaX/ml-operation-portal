package com.thitsaworks.operation_portal.core.hub_services.support;

public record SettlementAccount(
        Integer id,
        String reason,
        String state,
        NetSettlementAmount netSettlementAmount
) {}
