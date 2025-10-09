package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class PutUpdateParticipantLimit {

    public record Request(
            @JsonProperty("currency") String currency,
            @JsonProperty("limit") Limit limit
    ) {

    }

    public record Response(
            @JsonProperty("currency") String currency,
            @JsonProperty("limit") Limit limit
    ) {

    }

    public record Limit(
            @JsonProperty("type") String type,
            @JsonProperty("value") BigDecimal value,
            @JsonProperty("alarmPercentage") Integer alarmPercentage
    ) {

    }

}