package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindow;

import java.util.List;

public class PutUpdateSettlement {

    public record Request(List<SettlementParticipant> participants) {}

    public record Response(
            @JsonProperty("id") String id,
            @JsonProperty("state") String state,
            @JsonProperty("createdDate") String createdDate,
            @JsonProperty("settlementWindows") List<SettlementWindow> settlementWindows,
            @JsonProperty("participants") List<SettlementParticipant> participants) {}

}

