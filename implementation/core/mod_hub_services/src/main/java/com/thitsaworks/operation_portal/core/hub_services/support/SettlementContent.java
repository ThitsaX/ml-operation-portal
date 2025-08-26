package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SettlementContent(
        @JsonProperty("id") Integer id,
        @JsonProperty("state") String state,
        @JsonProperty("ledgerAccountType") String ledgerAccountType,
        @JsonProperty("currencyId") String currencyId,
        @JsonProperty("createdDate") String createdDate,
        @JsonProperty("changedDate") String changedDate
) {}
