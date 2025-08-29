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
public class HubParticipant {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("name")
    String name;

    @JsonProperty("created")
    String created;

    @JsonProperty("isActive")
    Boolean isActive;

    @JsonProperty("links")
    Links links;

    @JsonProperty("accounts")
    List<SettlementAccount> accounts;

    @JsonProperty("isProxy")
    Boolean isProxy;

    public static class Links {

        @JsonProperty("self")
        String self;

    }
}
