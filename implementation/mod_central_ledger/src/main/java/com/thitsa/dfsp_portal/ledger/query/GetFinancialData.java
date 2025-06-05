package com.thitsa.dfsp_portal.ledger.query;

import com.thitsa.dfsp_portal.ledger.data.FinancialData;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.dfsp_portal.participant.exception.ParticipantUserNotFoundException;
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
