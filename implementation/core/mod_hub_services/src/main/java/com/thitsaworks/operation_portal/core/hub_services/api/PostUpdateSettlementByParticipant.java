package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementAmount;

public class PostUpdateSettlementByParticipant {

    public record Request(@JsonProperty("transferId") String transferId,
                          @JsonProperty("action") String action,
                          @JsonProperty("reason") String reason,
                          @JsonProperty("externalReference") String externalReference,
                          @JsonProperty("amount") SettlementAmount amount) {}

    public record Response(Boolean transferred) {}

}

