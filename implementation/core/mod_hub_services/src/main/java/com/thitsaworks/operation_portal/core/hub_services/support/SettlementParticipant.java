package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SettlementParticipant(
        @JsonProperty("id") Integer id,
        @JsonProperty("accounts")  List<SettlementAccount> accounts
) {}
