package com.thitsaworks.operation_portal.reporting.central_ledger.query;

import com.thitsaworks.operation_portal.reporting.central_ledger.data.FinancialData;
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

    GetFinancialData.Output execute(GetFinancialData.Input input);

}
