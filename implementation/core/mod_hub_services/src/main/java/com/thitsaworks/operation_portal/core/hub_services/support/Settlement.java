package com.thitsaworks.operation_portal.core.hub_services.support;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Settlement {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("createdDate")
    private String createdDate;

    @JsonProperty("changedDate")
    private String changedDate;

    @JsonProperty("settlementWindows")
    private List<SettlementWindow> settlementWindows;

    @JsonProperty("participants")
    private List<SettlementParticipant> participants;
}
