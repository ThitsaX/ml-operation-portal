package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;

public class PostParticipantBalance {

    public record Request(
            @JsonProperty("transferId")
            String transferId,

            @JsonProperty("externalReference")
            String externalReference,

            @JsonProperty("action")
            String action,

            @JsonProperty("reason")
            String reason,

            @JsonProperty("amount")
            Money amount) {
    }

    public record Response(
            @JsonProperty("accessKey") String accessKey,

            @JsonProperty("secretKey") String secretKey) {

    }

}

