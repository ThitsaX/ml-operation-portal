package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementParticipant;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindow;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowId;

import java.util.List;

public class PostCreateSettlement {

    public record Request(
            @JsonProperty("settlementModel") String settlementModel,
            @JsonProperty("reason") String reason,
            @JsonProperty("settlementWindows") List<SettlementWindowId> settlementWindows
    ) {}

    public record Response(
            @JsonProperty("id") Integer id,
            @JsonProperty("settlementModel") String settlementModel,
            @JsonProperty("state") String state,
            @JsonProperty("reason") String reason,
            @JsonProperty("createdDate") String createdDate,
            @JsonProperty("changedDate") String changedDate,
            @JsonProperty("settlementWindows") List<SettlementWindow> settlementWindows,
            @JsonProperty("participants") List<SettlementParticipant> participants
    ) {}

}

