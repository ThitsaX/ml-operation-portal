package com.thitsaworks.operation_portal.core.hub_services.support;

import java.math.BigDecimal;

public record NetSettlementAmount(
        BigDecimal amount,
        String currency
) {}
