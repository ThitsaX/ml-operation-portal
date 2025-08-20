package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record SettlementAmount(
        @JsonProperty("amount") BigDecimal amount,
        @JsonProperty("currency") String currency
) {}
