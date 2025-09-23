package com.thitsaworks.operation_portal.core.hub_services.api;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.math.BigDecimal;
import java.util.List;

public class GetParticipantAccountBalance {

    public record Request(String participantName) {}

    public record Response(List<AccountBalance> accountBalances) {

        @JsonCreator
        public static Response fromArray(List<AccountBalance> balances) {

            return new Response(balances);
        }

    }

    public record AccountBalance(

            Integer id,
            String ledgerAccountType,
            String currency,
            Boolean isActive,
            BigDecimal value,
            BigDecimal reservedValue,
            String changedDate) {}

}

