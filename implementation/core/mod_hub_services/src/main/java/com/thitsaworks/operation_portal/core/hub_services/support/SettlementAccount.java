package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementAccount {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("state")
    String state;

    @JsonProperty("reason")
    String reason;

    @JsonProperty("externalReference")
    String externalReference;

    @JsonProperty("createdDate")
    String createdDate;

    @JsonProperty("netSettlementAmount")
    Money netSettlementAmount;

}


