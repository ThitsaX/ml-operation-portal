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
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementWindow {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("state")
    String state;

    @JsonProperty("reason")
    String reason;

    @JsonProperty("createdDate")
    String createdDate;

    @JsonProperty("changedDate")
    String changedDate;

    @JsonProperty("content")
    List<SettlementContent> content;

}
