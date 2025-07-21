package com.thitsaworks.operation_portal.core.participant.query;

public interface FindAccountNumberByParticipantNameQuery {

    record Input(String participantName, String currencyId) {
    }

    record Output(String accountNumber) {
    }

    Output execute(Input input);

}
