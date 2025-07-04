package com.thitsaworks.operation_portal.core.participant.query;

public interface FindAccountNumberByDfspCode {

    record Input(String dfspCode, String currencyId) {
    }

    record Output(String accountNumber) {
    }

    Output execute(Input input);

}
