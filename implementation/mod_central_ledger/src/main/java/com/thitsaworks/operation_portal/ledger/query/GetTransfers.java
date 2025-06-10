package com.thitsaworks.operation_portal.ledger.query;

import com.thitsaworks.operation_portal.ledger.data.TransferData;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface GetTransfers {

    @Value
    class Input {

        private String fromDate;

        private String toDate;

        private String transferId;

        private String payerFspId;

        private String payeeFspId;

        private String payerIdentifierTypeId;

        private String payeeIdentifierTypeId;

        private String payerIdentifierValue;

        private String payeeIdentifierValue;

        private String currencyId;

        private String transferStateId;

        private String fspName;

        private String timeZone;

    }

    @Value
    class Output {

        private List<TransferData> transferInfoList;

        @Value
        public static class TransferInfo implements Serializable {

            private String transferId;

            private String state;

            private String type;

            private String currency;

            private BigDecimal amount;

            private String payerDfsp;

            private String payeeDfsp;

            private String settlementBatch;

            private String submittedOnDate;

        }

    }

    Output execute(Input input) throws Exception;

}
