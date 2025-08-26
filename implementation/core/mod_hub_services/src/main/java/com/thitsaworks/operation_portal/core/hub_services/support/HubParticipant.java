package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record HubParticipant(
        @JsonProperty("id") Integer id,
        @JsonProperty("name") String name,
        @JsonProperty("created") String created,
        @JsonProperty("isActive") Boolean isActive,
        @JsonProperty("links") Links links,
        @JsonProperty("accounts")  List<SettlementAccount> accounts,
        @JsonProperty("isProxy")  Boolean isProxy
) {
    public record Links( @JsonProperty("self") String self) { }
}
