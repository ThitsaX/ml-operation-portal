package com.thitsaworks.operation_portal.core.participant.query;

public interface FindAccountNumberByDfspCodeQuery {

    record Input(String dfspCode, String currencyId) {
    }

    record Output(String accountNumber) {
    }

    Output execute(Input input);

}
