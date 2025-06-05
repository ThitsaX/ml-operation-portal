package com.thitsa.dfsp_portal.ledger.query;

import com.thitsa.dfsp_portal.ledger.data.BusinessData;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

public interface GetTransferDetail {

    @Value
    class Input {

        private String transferId;

    }

    @Value
    class Output {

        private BusinessData businessData;

        @Value
        public static class BusinessInfo implements Serializable {

            private String transferId;

            private String state;

            private String type;

            private String currency;

            private BigDecimal amount;

            private String payer;

            private String payerDetails;

            private String payerDfsp;

            private String payee;

            private String payeeDetails;

            private String payeeDfsp;

            private String settlementBatch;

        }

    }

    Output execute(Input input) throws Exception;

}
