package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SettlementWindowId(
        @JsonProperty("id") Integer id
) {}
