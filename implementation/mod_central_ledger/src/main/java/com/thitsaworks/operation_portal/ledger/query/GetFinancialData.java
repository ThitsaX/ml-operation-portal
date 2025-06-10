package com.thitsaworks.operation_portal.ledger.query;

import com.thitsaworks.operation_portal.ledger.data.FinancialData;
import com.thitsaworks.operation_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.participant.exception.ParticipantUserNotFoundException;
import lombok.Value;

import java.util.List;

public interface GetFinancialData {

    @Value
    class Input {

        private String fspID;

    }

    @Value
    class Output {

        private List<FinancialData> financialData;

    }

    GetFinancialData.Output execute(GetFinancialData.Input input) throws ParticipantUserNotFoundException,
            ParticipantNotFoundException;

}
