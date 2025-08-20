package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SettlementWindow(
        @JsonProperty("id") Integer id,
        @JsonProperty("reason") String reason,
        @JsonProperty("state") String state,
        @JsonProperty("createdDate") String createdDate,
        @JsonProperty("changedDate") String changedDate,
        @JsonProperty("content") List<SettlementContent> content
) {}
