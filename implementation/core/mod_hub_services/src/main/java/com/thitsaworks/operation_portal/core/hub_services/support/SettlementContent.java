package com.thitsaworks.operation_portal.core.hub_services.support;

public record SettlementContent(
        Integer id,
        String state,
        String ledgerAccountType,
        String currencyId,
        String createdDate,
        String changedDate,
        Integer settlementId
) {}
