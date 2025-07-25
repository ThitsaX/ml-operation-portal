package com.thitsaworks.operation_portal.core.hub_services.api;

import java.util.List;

public class GetParticipant {

    public record Request(String participantName) { }

    public record Response(

        String name, String id, String created, int isActive, Links links, List<Account> accounts, int isProxy) {

        public record Links(String self) { }

        public record Account(int id,
                              String ledgerAccountType,
                              String currency,
                              int isActive,
                              String createdDate,
                              String createdBy) {

        }

    }

}

