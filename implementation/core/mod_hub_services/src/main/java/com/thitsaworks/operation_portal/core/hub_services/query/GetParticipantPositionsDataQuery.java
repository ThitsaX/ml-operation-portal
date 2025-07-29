package com.thitsaworks.operation_portal.core.hub_services.query;


import com.thitsaworks.operation_portal.core.hub_services.data.FinancialData;
import lombok.Value;

import java.util.List;

public interface GetParticipantPositionsDataQuery {

    @Value
    class Input {

        private String fspID;

    }

    @Value
    class Output {

        private List<FinancialData> financialData;

    }

    Output execute(Input input);

}
