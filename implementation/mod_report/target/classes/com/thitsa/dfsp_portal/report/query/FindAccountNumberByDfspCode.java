package com.thitsa.dfsp_portal.report.query;

import lombok.Value;

public interface FindAccountNumberByDfspCode {

    @Value
    class Input {

        private String dfspCode;
        private String currencyId;

    }

    @Value
    class Output {

        private  String accountNumber;

    }

    Output execute(Input input);

}
