package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAction;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAmount;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementState;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindow;

import java.util.List;

public class PostUpdateSettlementByParticipant {

    public record Request(
            @JsonProperty("action") String action,
            @JsonProperty("reason") String reason,
            @JsonProperty("externalReference") String externalReference,
            @JsonProperty("amount") SettlementAmount amount,
            @JsonProperty("transferId") String transferId) {}

    public record Response(
            Boolean transferred) {}

}

