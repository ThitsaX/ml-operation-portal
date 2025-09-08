package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thitsaworks.operation_portal.component.fspiop.model.ExtensionList;
import com.thitsaworks.operation_portal.component.fspiop.model.Money;

public class PutParticipantStatus {

    public record Request(String participantName,

                          int participantCurrencyId,

                          boolean isActive) { }

    public record Response(String participantName,

                           String participantCurrencyId,

                           boolean isActive) {

    }

}

