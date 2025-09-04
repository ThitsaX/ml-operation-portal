package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Settlement(@JsonProperty("id") Integer id,
                         @JsonProperty("state") String state,
                         @JsonProperty("reason") String reason,
                         @JsonProperty("createdDate") String createdDate,
                         @JsonProperty("changedDate") String changedDate,
                         @JsonProperty("settlementWindows") List<SettlementWindow> settlementWindows,
                         @JsonProperty("participants") List<SettlementParticipant> participants) {
}
