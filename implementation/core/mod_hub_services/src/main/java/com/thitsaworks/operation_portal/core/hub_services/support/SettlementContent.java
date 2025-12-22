package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementContent {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("state")
    String state;

    @JsonProperty("ledgerAccountType")
    String ledgerAccountType;

    @JsonProperty("currencyId")
    String currencyId;

    @JsonProperty("createdDate")
    String createdDate;

    @JsonProperty("changedDate")
    String changedDate;

    @JsonProperty("closedDate")
    String closedDate;

}
